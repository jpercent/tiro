package syndeticlogic.tiro.trial;

import java.util.HashSet;

import syndeticlogic.tiro.controller.IOControllerFactory;
import syndeticlogic.tiro.controller.IOController;
import syndeticlogic.tiro.jdbc.BaseJdbcDao;
import syndeticlogic.tiro.model.Controller;
import syndeticlogic.tiro.model.Trial;
import syndeticlogic.tiro.monitor.SystemMonitor;

public class TrialRunnerFactory {
    private final IOControllerFactory builder;
    private final BaseJdbcDao jdbcDao;
    
    public TrialRunnerFactory(IOControllerFactory builder, BaseJdbcDao jdbcDao) {
        this.builder = builder;
        this.jdbcDao = jdbcDao;
    }
    
    public TrialRunner createTrialRunner(Trial trial, Controller[] controllerModels) {
        IOController[] controllers = new IOController[controllerModels.length];
        HashSet<String> devices = new HashSet<String>();
        int i = 0;
        for(Controller cmodel: controllerModels) 
            controllers[i++] = builder.createIOController(cmodel);
            
        SystemMonitor monitor = SystemMonitor.createSystemMonitor(devices.toArray(new String[devices.size()]));
        TrialResultCollector results = TrialResultCollector.createResultCollector(jdbcDao);
        return new TrialRunner(trial, controllerModels, monitor, controllers, results);
    }
}
