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
import syndeticlogic.tiro.monitor.SystemMonitor;

public abstract class TrialResultCollector {
    private final BaseJdbcDao baseJdbcDao;
    private final HashMap<Long, ControllerResultDescriptor> trials;
    private final ArrayList<IORecord> pqueue;
    private final Thread serializer;
    private final ReentrantLock lock;
    protected final Condition condition;
    private final int threshold;
    protected volatile boolean done;

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
    
	public class ControllerResultDescriptor {
        List<IORecord> ios;
        long duration;
        public ControllerResultDescriptor() {
            this.ios = new LinkedList<IORecord>();
        }
    }
	
	abstract void completeTrial(Long trialId, SystemMonitor monitor, long duration);
}
