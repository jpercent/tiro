   package syndeticlogic.tiro.trial;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import syndeticlogic.tiro.controller.IOController;
import syndeticlogic.tiro.controller.IOExecutor;
import syndeticlogic.tiro.model.Controller;
import syndeticlogic.tiro.model.IORecord;
import syndeticlogic.tiro.model.Trial;
import syndeticlogic.tiro.monitor.SystemMonitor;

public class TrialRunner {
    private static final Log log = LogFactory.getLog(TrialRunner.class);
    private final Trial trial;
    private final Controller[] controllers;
    private final SystemMonitor monitor;
    private final IOController[] ioControllers;
    private final TrialResultCollector results;
    private final Thread[] runThreads;
    
    public TrialRunner(Trial trial, Controller[] controllerModels, SystemMonitor monitor, IOController[] controllers, TrialResultCollector results) {
        this.trial = trial;
        this.controllers = controllerModels;
        this.monitor = monitor;
        this.ioControllers = controllers;
        this.results = results;
        runThreads = new Thread[controllers.length];
    }
    
    public void startTrial() {
        monitor.start();
        results.beginTrial(trial, controllers);
        for(int i = 0; i < ioControllers.length; i++) {
            runThreads[i] = startThread(ioControllers[i]);
        }
    }
    
    public Thread startThread(final IOController controller) {
        
        Thread runThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final long start = System.currentTimeMillis();
                    int count = 0;
                    IORecord[] iodescriptors = new IORecord[32678];
                    while (controller.notDone()) {
                        IOExecutor ioexe = controller.getNextIOExecutor();
                        ioexe.performIO();
                        iodescriptors[count] = ioexe.getIORecord();
                        count++;
                        if (count == 1024) {
                            results.addIORecords(controller.getId(), iodescriptors);
                            count = 0;
                        }
                    }
                    final long end = System.currentTimeMillis();
                    long duration = end - start;
                    if(count != 0) {
                        IORecord[] truncated = new IORecord[count];
                        System.arraycopy(iodescriptors, 0, truncated, 0, count);
                        results.addIORecords(controller.getId(), truncated);
                    }                    
                    results.completeController(controller.getId(), duration);
                } catch (Throwable t) {
                    log.error("Exception in TrialRunnder: ", t);
                    throw new RuntimeException(t);
                }
            }
        });
        runThread.start();
        return runThread;
    }

    public void waitForTrialCompletion() throws InterruptedException {
        for(int i = 0; i < runThreads.length; i++) {
            runThreads[i].join();
        }
        monitor.finish();
        results.completeTrial(trial.getId(), monitor, monitor.getDurationMillis());
    }
}
