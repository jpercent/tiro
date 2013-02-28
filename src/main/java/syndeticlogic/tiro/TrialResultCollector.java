package syndeticlogic.tiro;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import syndeticlogic.tiro.controller.ControllerMeta;
import syndeticlogic.tiro.controller.IORecord;

public class TrialResultCollector {
    final HashMap<Long, IOControllerResultDescriptor> trials;
    final Thread serializer;
    final ReentrantLock lock;
    final Condition condition;
    boolean done;

    public TrialResultCollector(TrialResultsJdbcDao jdbcDao, TrialMeta trialMeta, ControllerMeta controllerMeta) {
        
        trials = new HashMap<Long, IOControllerResultDescriptor>();
        lock = new ReentrantLock();
        condition = lock.newCondition();
        done = false;
        serializer = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    while (!done) {
                        try {
                            condition.await();

                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        });
    }
    
    public void addIORecord(IORecord ioDescriptor) {
        lock.lock();
        try {
            if (trials.containsKey(ioDescriptor.getControllerId())) {
                trials.get(ioDescriptor.getControllerId()).ios.add(ioDescriptor);
            } else {
                IOControllerResultDescriptor desc = new IOControllerResultDescriptor();
                desc.ios.add(ioDescriptor);
                trials.put(ioDescriptor.getControllerId(), desc);
            }
        } finally {
            lock.unlock();
        }
    }

    public void addIORecords(Long id, IORecord...ioDescriptor) {
        lock.lock();
        try {
            if (trials.containsKey(id)) {
                trials.get(id).ios.addAll(Arrays.asList(ioDescriptor));
            } else {
                IOControllerResultDescriptor desc = new IOControllerResultDescriptor();
                desc.ios.addAll(Arrays.asList(ioDescriptor));
                trials.put(id, desc);
            }
        } finally {
            lock.unlock();
        }
    }

    public void completeController(Long controllerId, long duration) {
        lock.lock();
        try {
            if (trials.containsKey(controllerId)) {
                trials.get(controllerId).duration = duration;
            } else {
                throw new RuntimeException("attempted to complete a trial that didn't create any records");
            }
        } finally {
            lock.unlock();
        }
    }

    public void completeTrial(TrialMonitor monitor) {
        
    }
    
    public class IOControllerResultDescriptor {
        List<IORecord> ios;
        long duration;
        public IOControllerResultDescriptor() {
            this.ios = new LinkedList<IORecord>();
        }
    }
}
