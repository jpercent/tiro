package syndeticlogic.tiro.jdbc;

import java.util.Properties;

public class SQLStatements {
    private final String trialsMeta;
    private final String controllersMeta;
    private final String trials;
    private final String controllers;
    private final String ioRecords;
    private final String ioStats;
    private final String memoryStats;
    private final String cpuStats;
    private final String aggregateStats;

    private final String createTrialsMeta;
    private final String createControllersMeta;
    private final String createTrials;
    private final String createControllers;
    private final String createIORecords;
    private final String createIOStats;
    private final String createMemoryStats;
    private final String createCpuStats;
    private final String createAggregateStats;
    
    private final String dropTrialsMeta;
    private final String dropControllersMeta;
    private final String dropTrials;
    private final String dropControllers;
    private final String dropIORecords;
    private final String dropIOStats;
    private final String dropMemoryStats;
    private final String dropCpuStats;
    private final String dropAggregateStats;
    
    private final String insertTrialMeta;
    private final String insertControllerMeta;
    private final String insertTrial;
    private final String insertController;
    private final String insertIORecord;
    private final String insertIOStats;
    private final String insertMemoryStats;
    private final String insertCpuStats;
    private final String insertAggregateStats;
    
    private final String selectTrialsMetaLast;
    private final String selectControllersMetaLast;
    private final String selectTrialsLast;
    private final String selectControllersLast;
    private final String selectIORecordsLast;
    private final String selectIOStatsLast;
    private final String selectMemoryStatsLast;
    private final String selectCpuStatsLast;
    private final String selectAggregateStatsLast;
    
    private final String completeTrial;
    private final String completeController;

    public SQLStatements(Properties config) {
        
        this.trialsMeta = config.getProperty("trials-meta");
        this.controllersMeta = config.getProperty("controllers-meta");
        this.trials = config.getProperty("trials");
        this.controllers = config.getProperty("controllers");
        this.ioRecords = config.getProperty("io-records");
        this.memoryStats = config.getProperty("memory-stats");
        this.ioStats = config.getProperty("io-stats");
        this.cpuStats = config.getProperty("cpu-stats");
        this.aggregateStats = config.getProperty("aggregated-stats");
        assert this.trialsMeta != null && this.controllersMeta != null && this.trials != null && this.controllers != null
        		&& this.ioRecords != null && this.memoryStats != null &&  this.ioStats != null && this.cpuStats != null
        		&& this.aggregateStats != null;
        
        this.createTrialsMeta = config.getProperty("create-trials-meta");
        this.createControllersMeta = config.getProperty("create-controllers-meta");
        this.createTrials = config.getProperty("create-trials");
        this.createControllers = config.getProperty("create-controllers");
        this.createIORecords = config.getProperty("create-iorecords");
        this.createIOStats = config.getProperty("create-io-stats");
        this.createMemoryStats = config.getProperty("create-memory-stats");
        this.createCpuStats = config.getProperty("create-cpu-stats");
        this.createAggregateStats = config.getProperty("create-aggregate-stats");
        assert createTrialsMeta != null && createTrials != null && createControllersMeta !=null && createControllers != null 
               && createIORecords != null && createIOStats != null && createMemoryStats != null && createCpuStats != null
               && createAggregateStats != null;

        this.dropTrialsMeta = config.getProperty("drop-trials-meta");
        this.dropControllersMeta = config.getProperty("drop-controllers-meta");
        this.dropTrials = config.getProperty("drop-trials");
        this.dropControllers = config.getProperty("drop-controllers");
        this.dropIORecords = config.getProperty("drop-io-records");
        this.dropMemoryStats = config.getProperty("drop-memory-stats");
        this.dropIOStats = config.getProperty("drop-io-stats");
        this.dropCpuStats = config.getProperty("drop-cpu-stats");
        this.dropAggregateStats = config.getProperty("drop-aggregated-stats");
        assert this.dropTrialsMeta != null && this.dropControllersMeta != null && this.dropTrials != null && this.dropControllers != null
        		&& this.dropIORecords != null && this.dropMemoryStats != null &&  this.dropIOStats != null && this.dropCpuStats != null
        		&& this.dropAggregateStats != null;
        
        this.insertTrialMeta = config.getProperty("insert-trial-meta"); 
        this.insertControllerMeta = config.getProperty("insert-controller-meta"); 
        this.insertTrial = config.getProperty("insert-trial"); 
        this.insertController = config.getProperty("insert-controller"); 
        this.insertIORecord = config.getProperty("insert-iorecord");
        this.insertIOStats = config.getProperty("insert-io-stats");
        this.insertMemoryStats = config.getProperty("insert-memory-stats");
        this.insertCpuStats = config.getProperty("insert-cpu-stats");
        this.insertAggregateStats = config.getProperty("insert-aggregate-stats");
        assert insertTrialMeta != null && insertControllerMeta != null && insertTrial != null && insertController != null
               && insertIORecord != null && insertMemoryStats != null && insertIOStats != null && insertCpuStats != null
               && insertAggregateStats != null;
        
        this.selectTrialsMetaLast = config.getProperty("select-trials-meta-last-id");
        this.selectControllersMetaLast = config.getProperty("select-controllers-meta-last-id");
        this.selectTrialsLast = config.getProperty("select-trials-last-id");
        this.selectControllersLast = config.getProperty("select-controllers-last-id");
        this.selectIORecordsLast = config.getProperty("select-iorecords-last-id");
        this.selectIOStatsLast = config.getProperty("select-io-stats-last-id");
        this.selectMemoryStatsLast = config.getProperty("select-memory-stats-last-id");
        this.selectCpuStatsLast = config.getProperty("select-cpu-stats-last-id");
        this.selectAggregateStatsLast = config.getProperty("select-aggregate-stats-last-id");
        assert selectTrialsMetaLast != null && selectControllersMetaLast != null && selectTrialsLast != null 
               && selectControllersLast != null && selectIORecordsLast != null && selectIOStatsLast != null 
               && selectMemoryStatsLast != null && selectCpuStatsLast != null && selectAggregateStatsLast != null;
        
        this.completeTrial = config.getProperty("complete-trial");
        this.completeController = config.getProperty("complete-controller");
        assert completeTrial != null && completeController != null; 
    }

	public String getTrialsMeta() {
		return trialsMeta;
	}

	public String getControllersMeta() {
		return controllersMeta;
	}

	public String getTrials() {
		return trials;
	}

	public String getControllers() {
		return controllers;
	}

	public String getIoRecords() {
		return ioRecords;
	}

	public String getIoStats() {
		return ioStats;
	}

	public String getMemoryStats() {
		return memoryStats;
	}

	public String getCpuStats() {
		return cpuStats;
	}

	public String getAggregateStats() {
		return aggregateStats;
	}

	public String getCreateTrialsMeta() {
		return createTrialsMeta;
	}

	public String getCreateControllersMeta() {
		return createControllersMeta;
	}

	public String getCreateTrials() {
		return createTrials;
	}

	public String getCreateControllers() {
		return createControllers;
	}

	public String getCreateIORecords() {
		return createIORecords;
	}

	public String getCreateIOStats() {
		return createIOStats;
	}

	public String getCreateMemoryStats() {
		return createMemoryStats;
	}

	public String getCreateCpuStats() {
		return createCpuStats;
	}

	public String getCreateAggregateStats() {
		return createAggregateStats;
	}

	public String getDropTrialsMeta() {
		return dropTrialsMeta;
	}

	public String getDropControllersMeta() {
		return dropControllersMeta;
	}

	public String getDropTrials() {
		return dropTrials;
	}

	public String getDropControllers() {
		return dropControllers;
	}

	public String getDropIORecords() {
		return dropIORecords;
	}

	public String getDropIOStats() {
		return dropIOStats;
	}

	public String getDropMemoryStats() {
		return dropMemoryStats;
	}

	public String getDropCpuStats() {
		return dropCpuStats;
	}

	public String getDropAggregateStats() {
		return dropAggregateStats;
	}

	public String getInsertTrialMeta() {
		return insertTrialMeta;
	}

	public String getInsertControllerMeta() {
		return insertControllerMeta;
	}

	public String getInsertTrial() {
		return insertTrial;
	}

	public String getInsertController() {
		return insertController;
	}

	public String getInsertIORecord() {
		return insertIORecord;
	}

	public String getInsertIOStats() {
		return insertIOStats;
	}

	public String getInsertMemoryStats() {
		return insertMemoryStats;
	}

	public String getInsertCpuStats() {
		return insertCpuStats;
	}

	public String getInsertAggregateStats() {
		return insertAggregateStats;
	}

	public String getSelectTrialsMetaLast() {
		return selectTrialsMetaLast;
	}

	public String getSelectControllersMetaLast() {
		return selectControllersMetaLast;
	}

	public String getSelectTrialsLast() {
		return selectTrialsLast;
	}

	public String getSelectControllersLast() {
		return selectControllersLast;
	}

	public String getSelectIORecordsLast() {
		return selectIORecordsLast;
	}

	public String getSelectIOStatsLast() {
		return selectIOStatsLast;
	}

	public String getSelectMemoryStatsLast() {
		return selectMemoryStatsLast;
	}

	public String getSelectCpuStatsLast() {
		return selectCpuStatsLast;
	}

	public String getSelectAggregateStatsLast() {
		return selectAggregateStatsLast;
	}

	public String getCompleteTrial() {
		return completeTrial;
	}

	public String getCompleteController() {
		return completeController;
	}
}
