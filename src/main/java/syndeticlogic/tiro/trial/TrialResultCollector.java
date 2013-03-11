package syndeticlogic.tiro.trial;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import syndeticlogic.tiro.monitor.SystemMonitor;
import syndeticlogic.tiro.persistence.AggregatedIOStats;
import syndeticlogic.tiro.persistence.Controller;
import syndeticlogic.tiro.persistence.IORecord;
import syndeticlogic.tiro.persistence.IOStats;
import syndeticlogic.tiro.persistence.JdbcDao;
import syndeticlogic.tiro.persistence.Trial;

public class TrialResultCollector {
    private final JdbcDao jdbcDao;
    private final HashMap<Long, ControllerResultDescriptor> trials;
    private final ArrayList<IORecord> pqueue;
    private final Thread serializer;
    private final ReentrantLock lock;
    private final Condition condition;
    private final int threshold;
    private volatile boolean done;

    public TrialResultCollector(JdbcDao jdbcDaoa) {    
        this.jdbcDao = jdbcDaoa;
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
                            	jdbcDao.insertIORecord(pqueue);
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
        jdbcDao.insertTrial(trial);
        jdbcDao.insertControllers(controllers);
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
                jdbcDao.completeController(controllerId, duration);
                flush(desc, /* force = */ true);
            } else {
                throw new RuntimeException("attempted to complete a trial that didn't create any records");
            }
        } finally {
            lock.unlock();
        }
    }

    public void completeTrial(Long trialId, SystemMonitor monitor, long duration) {
        HashMap<String, IOStats> iostatsByDevice = new HashMap<String, IOStats>();
        for(IOStats iostat : monitor.getIOStats()) {
        	jdbcDao.insertIOStats(iostat, trialId);
            iostatsByDevice.put(iostat.getDevice(), iostat);
        }
        jdbcDao.insertMemoryStats(monitor.getMemoryStats(), trialId);
        jdbcDao.insertCpuStats(monitor.getCpuStats(), trialId);
        AggregatedIOStats aggregatedIOStats = new AggregatedIOStats(iostatsByDevice);
        jdbcDao.completeTrial(aggregatedIOStats, monitor.getMemoryStats(), monitor.getCpuStats(), duration, trialId);
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
