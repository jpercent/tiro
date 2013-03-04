package syndeticlogic.tiro.trial;

import java.util.HashSet;

import syndeticlogic.tiro.controller.IOControllerFactory;
import syndeticlogic.tiro.controller.IOController;
import syndeticlogic.tiro.monitor.SystemMonitor;
import syndeticlogic.tiro.persistence.Controller;
import syndeticlogic.tiro.persistence.JdbcDao;

public class TrialRunnerFactory {
    private final IOControllerFactory builder;
    private final JdbcDao jdbcDao;
    
    public TrialRunnerFactory(IOControllerFactory builder, JdbcDao jdbcDao) {
        this.builder = builder;
        this.jdbcDao = jdbcDao;
    }
    
    public TrialRunner createTrialRunner(Controller[] controllerModels) {
        IOController[] controllers = new IOController[controllerModels.length];
        HashSet<String> devices = new HashSet<String>();
        int i = 0;
        for(Controller cmodel: controllerModels) 
            controllers[i++] = builder.createIOController(cmodel);
            
        SystemMonitor monitor = SystemMonitor.createSystemMonitor((String[])devices.toArray());
        TrialResultCollector results = new TrialResultCollector(jdbcDao);
        return new TrialRunner(monitor, controllers, results);
    }
}
