package syndeticlogic.tiro.trial;

import java.util.HashSet;

import syndeticlogic.tiro.controller.IOControllerFactory;
import syndeticlogic.tiro.controller.IOController;
import syndeticlogic.tiro.monitor.SystemMonitor;
import syndeticlogic.tiro.persistence.Controller;
import syndeticlogic.tiro.persistence.Trial;
import syndeticlogic.tiro.persistence.jdbc.BaseJdbcDao;

public class TrialRunnerFactory {
    private final IOControllerFactory builder;
    private final BaseJdbcDao baseJdbcDao;
    
    public TrialRunnerFactory(IOControllerFactory builder, BaseJdbcDao baseJdbcDao) {
        this.builder = builder;
        this.baseJdbcDao = baseJdbcDao;
    }
    
    public TrialRunner createTrialRunner(Trial trial, Controller[] controllerModels) {
        IOController[] controllers = new IOController[controllerModels.length];
        HashSet<String> devices = new HashSet<String>();
        int i = 0;
        for(Controller cmodel: controllerModels) 
            controllers[i++] = builder.createIOController(cmodel);
            
        SystemMonitor monitor = SystemMonitor.createSystemMonitor(devices.toArray(new String[devices.size()]));
        TrialResultCollector results = new TrialResultCollector(baseJdbcDao);
        return new TrialRunner(trial, controllerModels, monitor, controllers, results);
    }
}
