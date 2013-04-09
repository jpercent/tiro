package syndeticlogic.tiro.trial;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import syndeticlogic.tiro.jdbc.BaseJdbcDao;
import syndeticlogic.tiro.model.Controller;
import syndeticlogic.tiro.model.IORecord;
import syndeticlogic.tiro.model.Trial;
import syndeticlogic.tiro.monitor.AbstractMonitor;
import syndeticlogic.tiro.monitor.SystemMonitor;
import syndeticlogic.tiro.stat.LinuxAggregatedIOStats;
import syndeticlogic.tiro.stat.LinuxCpuStats;
import syndeticlogic.tiro.stat.LinuxIOStats;
import syndeticlogic.tiro.stat.LinuxMemoryStats;
import syndeticlogic.tiro.stat.OsxAggregatedIOStats;
import syndeticlogic.tiro.stat.OsxCpuStats;
import syndeticlogic.tiro.stat.OsxIOStats;
import syndeticlogic.tiro.stat.OsxMemoryStats;

public class TrialResultCollector {
    private final BaseJdbcDao baseJdbcDao;
    private final HashMap<Long, ControllerResultDescriptor> trials;
    private final ArrayList<IORecord> pqueue;
    private final Thread serializer;
    private final ReentrantLock lock;
    private final Condition condition;
    private final int threshold;
    private volatile boolean done;

    public TrialResultCollector(BaseJdbcDao jdbcDaoa) {    
        this.baseJdbcDao = jdbcDaoa;
        trials = new HashMap<Long, ControllerResultDescriptor>();
        lock = new ReentrantLock();
        condition = lock.newCondition();
        done = false;
        threshold = 32768;
        pqueue = new ArrayList<IORecord>(threshold);

        serializer = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    while (!done) {
                        try {
                            condition.await();
                            if(pqueue.size() > 0) {
                            	baseJdbcDao.insertIORecord(pqueue);
                            	pqueue.clear();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        });
        serializer.start();
    }
    
    public void beginTrial(Trial trial, Controller[] controllers) {
        baseJdbcDao.insertTrial(trial);
        baseJdbcDao.insertControllers(controllers);
    }
    
    public void addIORecord(IORecord ioDescriptor) {
        lock.lock();
        ControllerResultDescriptor desc;
        try {
            if (trials.containsKey(ioDescriptor.getControllerId())) {
                desc = trials.get(ioDescriptor.getControllerId());
                desc.ios.add(ioDescriptor);
            } else {
                desc = new ControllerResultDescriptor();
                desc.ios.add(ioDescriptor);
                trials.put(ioDescriptor.getControllerId(), desc);
            }
            flush(desc, false);
        } finally {
            lock.unlock();
        }
    }

    public void addIORecords(Long id, IORecord...ioDescriptor) {
        lock.lock();
        ControllerResultDescriptor desc;
        try {
            if (trials.containsKey(id)) {
            	desc = trials.get(id);
                desc.ios.addAll(Arrays.asList(ioDescriptor));
            } else {
                desc = new ControllerResultDescriptor();
                desc.ios.addAll(Arrays.asList(ioDescriptor));
                trials.put(id, desc);
            }
            flush(desc, false);

        } finally {
            lock.unlock();
        }
    }

    protected void flush(ControllerResultDescriptor desc, boolean force) {
        if(force || desc.ios.size() > threshold) {
        	pqueue.addAll(desc.ios);
        	desc.ios.clear();
        	condition.signalAll();
        }
    }
    
    public void completeController(Long controllerId, long duration) {
        lock.lock();
        ControllerResultDescriptor desc;
        try {
            if (trials.containsKey(controllerId)) {
                desc = trials.get(controllerId);
                desc.duration = duration;
                baseJdbcDao.completeController(controllerId, duration);
                flush(desc, /* force = */ true);
            } else {
                throw new RuntimeException("attempted to complete a trial that didn't create any records");
            }
        } finally {
            lock.unlock();
        }
    }

    public void completeTrial(Long trialId, SystemMonitor monitor, long duration) {
        if(AbstractMonitor.getPlatform() == AbstractMonitor.Platform.Linux) {
        	completeTrial(trialId, (LinuxMemoryStats)monitor.getMemoryStats(), (LinuxIOStats[])monitor.getIOStats(), (LinuxCpuStats)monitor.getCpuStats(), duration);
        } else if(AbstractMonitor.getPlatform() == AbstractMonitor.Platform.Windows) {
        	completeTrial(trialId, (OsxMemoryStats)monitor.getMemoryStats(), (OsxIOStats[])monitor.getIOStats(), (OsxCpuStats)monitor.getCpuStats(), duration);
        } else {
        	throw new RuntimeException("Unsupported platform");
        }
    }
    
    private void completeTrial(Long trialId, LinuxMemoryStats memoryStats, LinuxIOStats[] ioStats, LinuxCpuStats cpuStats, long duration) {
        HashMap<String, LinuxIOStats> iostatsByDevice = new HashMap<String, LinuxIOStats>();
        for(LinuxIOStats iostat : ioStats) {
        	baseJdbcDao.insertIOStats(iostat, trialId);
            iostatsByDevice.put(iostat.getDevice(), iostat);
        }
        baseJdbcDao.insertMemoryStats(memoryStats, trialId);
        baseJdbcDao.insertCpuStats(cpuStats, trialId);
        LinuxAggregatedIOStats aggregatedIOStats = new LinuxAggregatedIOStats(iostatsByDevice);
        baseJdbcDao.completeTrial(aggregatedIOStats, memoryStats, cpuStats, duration, trialId);
        done = true;
        condition.signalAll();
		
	}
    
    private void completeTrial(Long trialId, OsxMemoryStats memoryStats, OsxIOStats[] ioStats, OsxCpuStats cpuStats, long duration) {
        HashMap<String, OsxIOStats> iostatsByDevice = new HashMap<String, OsxIOStats>();
        for(OsxIOStats iostat : ioStats) {
        	baseJdbcDao.insertIOStats(iostat, trialId);
            iostatsByDevice.put(iostat.getDevice(), iostat);
        }
        baseJdbcDao.insertMemoryStats(memoryStats, trialId);
        baseJdbcDao.insertCpuStats(cpuStats, trialId);
        OsxAggregatedIOStats aggregatedIOStats = new OsxAggregatedIOStats(iostatsByDevice);
        baseJdbcDao.completeTrial(aggregatedIOStats, memoryStats, cpuStats, duration, trialId);
        done = true;
        condition.signalAll();
		
	}
    
	public class ControllerResultDescriptor {
        List<IORecord> ios;
        long duration;
        public ControllerResultDescriptor() {
            this.ios = new LinkedList<IORecord>();
        }
    }
}
