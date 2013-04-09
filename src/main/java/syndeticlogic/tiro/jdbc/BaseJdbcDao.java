package syndeticlogic.tiro.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import syndeticlogic.tiro.model.Controller;
import syndeticlogic.tiro.model.ControllerMeta;
import syndeticlogic.tiro.model.IORecord;
import syndeticlogic.tiro.model.Trial;
import syndeticlogic.tiro.model.TrialMeta;
import syndeticlogic.tiro.stat.LinuxAggregatedIOStats;
import syndeticlogic.tiro.stat.LinuxCpuStats;
import syndeticlogic.tiro.stat.LinuxIOStats;
import syndeticlogic.tiro.stat.LinuxMemoryStats;
import syndeticlogic.tiro.stat.OsxAggregatedIOStats;
import syndeticlogic.tiro.stat.OsxCpuStats;
import syndeticlogic.tiro.stat.OsxIOStats;
import syndeticlogic.tiro.stat.OsxMemoryStats;

import java.sql.PreparedStatement;

public class BaseJdbcDao {
    protected final JdbcTemplate jdbcTemplate;
    protected final String driverClassName;
    protected final String jdbcUrl;
    
    protected final String createTrialsMeta;
    protected final String createControllersMeta;
    protected final String createTrials;
    protected final String createControllers;
    protected final String createIORecords;
    protected final String createIOStats;
    protected final String createMemoryStats;
    protected final String createCpuStats;
    protected final String createAggregateStats;
    
    protected final String insertTrialMeta;
    protected final String insertControllerMeta;
    protected final String insertTrial;
    protected final String insertController;
    protected final String insertIORecord;
    protected final String insertIOStats;
    protected final String insertMemoryStats;
    protected final String insertCpuStats;
    protected final String insertAggregateStats;
    
    protected final String selectTrialsMetaLast;
    protected final String selectControllersMetaLast;
    protected final String selectTrialsLast;
    protected final String selectControllersLast;
    protected final String selectIORecordsLast;
    protected final String selectIOStatsLast;
    protected final String selectMemoryStatsLast;
    protected final String selectCpuStatsLast;
    protected final String selectAggregateStatsLast;
    
    protected final String completeTrial;
    protected final String completeController;
    
    protected long trialsId;
    protected long controllersId;
    protected long ioRecordsId;
    protected long ioStatsId;
    protected long memoryStatsId;
    protected long cpuStatsId;
    protected long aggregateStatsId;
    // derby driverclassname == org.apache.derby.jdbc.EmbeddedDriver
    // derby jdbcUrl == jdbc:derby://localhost:21529/tmp/catena/trial_results;create=true
    // postgres driverClassName == org.postgresql.Driver
    // postres jdbcUrl == jdbc:postgresql://localhost:5432/catena?user=james&password=password
    // sqlite driverClassName == org.sqlite.JDBC
    // sqlite jdburl == jdbc:sqlite:trial-result.db;foreign keys=true?user=james&password=password
    public BaseJdbcDao(Properties config) {
        this.driverClassName = config.getProperty("driver-class-name");
        this.jdbcUrl = config.getProperty("jdbc-url");
        assert driverClassName != null && jdbcUrl != null;
        
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
                
        // create data source connection
        DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName(driverClassName);
        source.setUrl(jdbcUrl);
        jdbcTemplate = new JdbcTemplate(source);
        trialsId = 0;
        controllersId = 0;
        ioRecordsId = 0;
        ioStatsId = 0;
        memoryStatsId = 0;
        cpuStatsId = 0;
        aggregateStatsId = 0;
    }
    
    protected long getId(String query) {
        long id = -1;
        try {
            id = jdbcTemplate.queryForObject(query, Long.class);
        } catch (EmptyResultDataAccessException e) {
        }
        System.out.println("id == "+id);
        if(id != -1) id++;
        else id = 1;
        return id;
    }
    
    public void initialize() {
        synchronized(this) {
            controllersId = getId(selectControllersLast);
            ioRecordsId = getId(selectIORecordsLast);
            ioStatsId = getId(selectIOStatsLast);
            memoryStatsId = getId(selectMemoryStatsLast);
            cpuStatsId = getId(selectCpuStatsLast);
            aggregateStatsId = getId(selectAggregateStatsLast);
        }
    }
    
    public void createTables() {
        jdbcTemplate.execute(createTrialsMeta);
        jdbcTemplate.execute(createControllersMeta);
        jdbcTemplate.execute(createTrials);
        jdbcTemplate.execute(createControllers);
        jdbcTemplate.execute(createIORecords);
        jdbcTemplate.execute(createIOStats);
        jdbcTemplate.execute(createMemoryStats);
        jdbcTemplate.execute(createCpuStats);
        System.out.println("Linux? ");
        jdbcTemplate.execute(createAggregateStats);
        System.out.println("Linux sucks");
    }
    
    public void insertTrialMeta(TrialMeta trialMeta) {
        System.out.println(insertTrialMeta);
        jdbcTemplate.update(insertTrialMeta, trialMeta.getName());
        trialMeta.setId(jdbcTemplate.queryForLong(selectTrialsMetaLast));
    }

    public void insertControllerMeta(ControllerMeta controllerMeta) {
        jdbcTemplate.update(insertControllerMeta, controllerMeta.getControllerTypeName(), controllerMeta.getExecutorTypeName(), 
                controllerMeta.getMemoryTypeName(), controllerMeta.getDevice());        
        controllerMeta.setId(jdbcTemplate.queryForLong(selectControllersMetaLast));
    }
    
    public void insertTrial(Trial trial) {
        synchronized(this) {
            trial.setId(trialsId++);
        }
        jdbcTemplate.update(insertTrial, trial.getId() , trial.getMeta().getId());
    }
    
    public void insertController(Controller controller) { 
        synchronized(this) {
            controller.setId(controllersId++);
        }
        jdbcTemplate.update(insertController, controller.getId(), controller.getTrialMeta().getId(), controller.getControllerMeta().getId());
    }
    
    public void insertControllers(final Controller[] controllers) {
        final long id;
        synchronized(this) {
            id = controllersId;
            controllersId += controllers.length;
        }
        jdbcTemplate.batchUpdate(insertIORecord, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                controllers[i].setId(id+i);
                ps.setLong(1, id+i);
                ps.setLong(2, controllers[i].getTrialMeta().getId());
                ps.setLong(3, controllers[i].getTrialMeta().getId());
            }
            @Override
            public int getBatchSize() {
                return controllers.length;
            }
        });
    }
    
    public void completeController(long controllerId, long duration) {
        jdbcTemplate.update(completeController, duration);
    }
    
    public void insertIORecord(final List<IORecord> records) {
        final long id;
        synchronized(this) {
            id = ioRecordsId;
            ioRecordsId += records.size();
        }
        jdbcTemplate.batchUpdate(insertIORecord, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id+i);
                ps.setLong(2, records.get(i).getControllerId());
                ps.setLong(3, records.get(i).getLba());
                ps.setLong(4, records.get(i).getDuration());
                ps.setInt(5, records.get(i).getSize());
                ps.setString(6, records.get(i).getStrategyName());
            }
            @Override
            public int getBatchSize() {
                return records.size();
            }
        });
    }
    
    public void insertMemoryStats(OsxMemoryStats monitor, final long trialId) {
        final List<Long> freePages = monitor.getRawFreePageMeasurements();
        final List<Long> activePages = monitor.getRawActivePageMeasurements();
        final List<Long> inactivePages = monitor.getRawInactivePagesMeasurements();
        final List<Long> wiredPages = monitor.getRawWiredPagesMeasurements();
        final List<Long> faultRoutineCalls = monitor.getRawNumberOfFaultRoutineCallMeasurements();
        final List<Long> copyOnWriteFaults = monitor.getRawCopyOnWriteFaultsMeasurements();
        final List<Long> zeroFilledPages = monitor.getRawZeroFilledPageMeasurements();
        final List<Long> reactivePages = monitor.getRawReactivatedPagesMeasurements();
        final List<Long> pageIns = monitor.getRawPageInMeasurements();
        final List<Long> pageOuts = monitor.getRawPageOutsMeasurements();
        final long id;
        synchronized(this) {
            id = memoryStatsId;
            memoryStatsId += freePages.size();
        }
        jdbcTemplate.batchUpdate(insertMemoryStats, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id+i);
                ps.setLong(2, trialId);
                ps.setLong(3, freePages.get(i));
                ps.setLong(4, activePages.get(i));
                ps.setLong(5, inactivePages.get(i));
                ps.setLong(6, wiredPages.get(i));
                ps.setLong(7, faultRoutineCalls.get(i));
                ps.setLong(8, copyOnWriteFaults.get(i));
                ps.setLong(9, zeroFilledPages.get(i));
                ps.setLong(10, reactivePages.get(i));
                ps.setLong(11, pageIns.get(i));
                ps.setLong(12, pageOuts.get(i));
            }
            @Override
            public int getBatchSize() {
                return freePages.size();
            }
        });
    }

    public void insertMemoryStats(final LinuxMemoryStats stats, final Long trialId) {
    	final List<Long> free = stats.getFree();
    	final List<Long> buffers = stats.getBuffers();
    	final List<Long> cached = stats.getCached();
    	final List<Long>  swapcached = stats.getSwapCached();
    	final List<Long>  active = stats.getActive();
    	final List<Long>  activeAnon = stats.getActiveAnon();
    	final List<Long>  activeFile = stats.getActiveFile();
    	final List<Long>  inactive = stats.getInactive();
    	final List<Long>  inactiveAnon= stats.getInactiveAnon();
    	final List<Long>  inactiveFile = stats.getInactiveFile();
    	final List<Long>  unevictable = stats.getUnevictable();
    	final List<Long>  swapTotal = stats.getSwapTotal();
    	final List<Long>  swapFree = stats.getSwapFree();
    	final List<Long>  dirty = stats.getDirty();
    	final List<Long>  writeback = stats.getWriteback();
    	final List<Long>  anon = stats.getAnon();
    	final List<Long>  slab = stats.getSlab();
    	final List<Long>  sreclaim = stats.getSreclaim();
    	final List<Long>  sunreclaim = stats.getSunreclaim();
    	final List<Long>  kernelStack = stats.getKernelStack();
    	final List<Long>  bounce = stats.getBounce();
    	final List<Long>  vmallocTotal = stats.getVmallocTotal();
    	final List<Long>  vmallocUsed = stats.getVmallocUsed();
    	final List<Long>  vmallocChunk = stats.getVmallocChunk();
    	final long id;
        synchronized(this) {
            id = memoryStatsId;
            memoryStatsId += free.size();
        }
        jdbcTemplate.batchUpdate(insertMemoryStats, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
            	int j = 1;
                ps.setLong(j++, id+i);
                ps.setLong(j++, trialId);
                ps.setLong(j++, free.get(i));
                ps.setLong(j++, buffers.get(i));
                ps.setLong(j++, cached.get(i));
                ps.setLong(j++, swapcached.get(i));
                ps.setLong(j++, active.get(i));
                ps.setLong(j++, activeAnon.get(i));
                ps.setLong(j++, activeFile.get(i));
                ps.setLong(j++, inactive.get(i));
                ps.setLong(j++, inactiveAnon.get(i));
                ps.setLong(j++, inactiveFile.get(i));
                ps.setLong(j++, unevictable.get(i));
                ps.setLong(j++, swapTotal.get(i));
                ps.setLong(j++, swapFree.get(i));
                ps.setLong(j++, dirty.get(i));
                ps.setLong(j++, writeback.get(i));
                ps.setLong(j++, anon.get(i));
                ps.setLong(j++, slab.get(i));
                ps.setLong(j++, sreclaim.get(i));
                ps.setLong(j++, sunreclaim.get(i));
                ps.setLong(j++, kernelStack.get(i));
                ps.setLong(j++, bounce.get(i));
                ps.setLong(j++, vmallocTotal.get(i));
                ps.setLong(j++, vmallocUsed.get(i));
                ps.setLong(j++, vmallocChunk.get(i));
            }
            @Override
            public int getBatchSize() {
                return free.size();
            }
        });
	}

    public void insertIOStats(OsxIOStats io, final long trialId) {
        final List<Double> kilobytesPerTransfer = io.getRawKiloBytesPerTranferMeasurements();
        final List<Double> transfersPerSecond = io.getRawTransfersPerSecond();
        final List<Double> megabytesPerSecond = io.getRawMegabytesPerSecond();
        final long id;
        
        synchronized(this) {
            id = ioStatsId;
            ioStatsId += megabytesPerSecond.size();
        }
        jdbcTemplate.batchUpdate(insertIOStats, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id+i);
                ps.setLong(2, trialId);
                ps.setDouble(3, kilobytesPerTransfer.get(i));
                ps.setDouble(4, transfersPerSecond.get(i));
                ps.setDouble(5, megabytesPerSecond.get(i));
            }
            @Override
            public int getBatchSize() {
                return megabytesPerSecond.size();
            }
        });
    }
    
	public void insertIOStats(final LinuxIOStats io, final Long trialId) {
        final List<Double> tps = io.getRawTps();
        final List<Double> kpsRead = io.getRawKpsRead();
        final List<Double> kpsWritten = io.getRawKpsWritten();
        final long id;
        
        synchronized(this) {
            id = ioStatsId;
            ioStatsId += tps.size();
        }
        jdbcTemplate.batchUpdate(insertIOStats, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id+i);
                ps.setLong(2, trialId);
                ps.setDouble(3, tps.get(i));
                ps.setDouble(4, kpsRead.get(i));
                ps.setDouble(5, kpsWritten.get(i));
            }
            @Override
            public int getBatchSize() {
                return tps.size();
            }
        });
	}

    public void insertCpuStats(OsxCpuStats cpu, final long trialId) {
        final List<Long> userMode = cpu.getRawUserModeTime();
        final List<Long> systemMode = cpu.getRawSystemModeTime();
        final List<Long> idleMode = cpu.getRawIdleModeTime();
        final long id;
        synchronized(this) {
            id = cpuStatsId;
            cpuStatsId += idleMode.size();
        }
        jdbcTemplate.batchUpdate(insertCpuStats, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id+i);
                ps.setLong(2, trialId);
                ps.setLong(3, userMode.get(i));
                ps.setLong(4, systemMode.get(i));
                ps.setLong(5, idleMode.get(i));
            }
            @Override
            public int getBatchSize() {
                return idleMode.size();
            }
        });
    }
    
	public void insertCpuStats(final LinuxCpuStats cpu, final Long trialId) {
		final List<Double> userMode = cpu.getRawUserModeTime();
		final List<Double> systemMode = cpu.getRawSystemModeTime();
		final List<Double> iowait = cpu.getRawIowaitTime();
		final List<Double> idleMode = cpu.getRawIdleModeTime();
		final long id;
		synchronized (this) {
			id = cpuStatsId;
			cpuStatsId += idleMode.size();
		}
		jdbcTemplate.batchUpdate(insertCpuStats,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setLong(1, id + i);
						ps.setLong(2, trialId);
						ps.setDouble(3, userMode.get(i));
						ps.setDouble(4, systemMode.get(i));
						ps.setDouble(4, iowait.get(i));
						ps.setDouble(5, idleMode.get(i));
					}

					@Override
					public int getBatchSize() {
						return idleMode.size();
					}
				});
	}

    public void completeTrial(OsxAggregatedIOStats ioStats, OsxMemoryStats memoryStats, OsxCpuStats cpuStats, long duration, long trialId) {
        jdbcTemplate.update(completeTrial, duration, ioStats.getAverageMegabytesPerSecond(), cpuStats.getAverageUserModeTime(), 
                cpuStats.getAverageSystemModeTime(), cpuStats.getAverageIdleModeTime(), memoryStats.getAverageFreePages(), 
                memoryStats.getAverageActivePages(), memoryStats.getAverageInactivePages(), memoryStats.getAverageWiredPages(), 
                memoryStats.getAverageNumberOfFaultRoutineCalls(), memoryStats.getAverageCopyOnWriteFaults(), memoryStats.getAverageZeroFilledPages(),
                memoryStats.getAveragePageIns(), memoryStats.getAveragePageOuts(), trialId);
    }

	public void completeTrial(LinuxAggregatedIOStats aggregatedIOStats, LinuxMemoryStats memoryStats, LinuxCpuStats cpuStats, long duration, Long trialId) {
        jdbcTemplate.update(completeTrial, duration, aggregatedIOStats.getAverageKpsRead(), cpuStats.getAverageUserModeTime(), 
                cpuStats.getAverageSystemModeTime(), cpuStats.getAverageIowaitTime(), cpuStats.getAverageIdleModeTime(),                 
                memoryStats.getFree(), memoryStats.getBuffers(), memoryStats.getCached(), memoryStats.getSwapCached(), memoryStats.getActive(), memoryStats.getActiveAnon(),
            	memoryStats.getActiveFile(), memoryStats.getInactive(), memoryStats.getInactiveAnon(), memoryStats.getInactiveFile(), memoryStats.getUnevictable(),
            	memoryStats.getSwapTotal(), memoryStats.getSwapFree(), memoryStats.getDirty(), memoryStats.getWriteback(), memoryStats.getAnon(), memoryStats.getSlab(),
            	memoryStats.getSreclaim(), memoryStats.getSunreclaim(), memoryStats.getKernelStack(), memoryStats.getBounce(), memoryStats.getVmallocTotal(), 
            	memoryStats.getVmallocUsed(), memoryStats.getVmallocChunk(), trialId);
	}
	
    public List<?> adHocQuery(String sql, RowMapper<?> rowMapper, Map<String, Object> args) {
        return jdbcTemplate.query(sql, rowMapper, args);
    }

    public List<?> adHocQuery(String sql, RowMapper<?> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }
}
