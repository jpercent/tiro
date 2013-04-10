package syndeticlogic.tiro.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import syndeticlogic.tiro.Tiro;
import syndeticlogic.tiro.model.Controller;
import syndeticlogic.tiro.model.ControllerMeta;
import syndeticlogic.tiro.model.IORecord;
import syndeticlogic.tiro.model.Trial;
import syndeticlogic.tiro.model.TrialMeta;

import java.sql.PreparedStatement;

public class BaseJdbcDao {
	private static final Log log = LogFactory.getLog(BaseJdbcDao.class); 
	protected final DriverManagerDataSource source;
    protected final JdbcTemplate jdbcTemplate;
    protected final String driverClassName;
    protected final String jdbcUrl;
    
    protected final String trialsMeta;
    protected final String controllersMeta;
    protected final String trials;
    protected final String controllers;
    protected final String ioRecords;
    protected final String ioStats;
    protected final String memoryStats;
    protected final String cpuStats;
    protected final String aggregateStats;

    protected final String createTrialsMeta;
    protected final String createControllersMeta;
    protected final String createTrials;
    protected final String createControllers;
    protected final String createIORecords;
    protected final String createIOStats;
    protected final String createMemoryStats;
    protected final String createCpuStats;
    protected final String createAggregateStats;
    
    protected final String dropTrialsMeta;
    protected final String dropControllersMeta;
    protected final String dropTrials;
    protected final String dropControllers;
    protected final String dropIORecords;
    protected final String dropIOStats;
    protected final String dropMemoryStats;
    protected final String dropCpuStats;
    protected final String dropAggregateStats;
    
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
                
        // create data source connection
        this.source = new DriverManagerDataSource();
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
    	if(needsInit()) {
    		createTables();
    	}
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
        jdbcTemplate.execute(createAggregateStats);
    }
    
	public boolean needsInit() {
		boolean ret = false;
		Connection connection=null;
		try {
			connection = source.getConnection();
			DatabaseMetaData dbm = connection.getMetaData();
			
			@SuppressWarnings("serial")
			LinkedList<String> tables = new LinkedList<String>() {{
				add(trialsMeta); add(controllersMeta); add(trials);
				add(controllers); add(ioRecords); add(memoryStats);
				add(ioStats); add(aggregateStats);
			}};
			
			for (String table : tables) {
				ResultSet tableMeta = dbm.getTables(null, null, table, null);
				if (!tableMeta.next()) {
					ret = true;
					break;
				}
			}
		} catch (SQLException e) {
			log.fatal("Recieved SQLException configuring Tiro", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if(connection != null)
					connection.close();
				} catch (SQLException e) {
					log.warn(" could not close connection", e);
				}
		}
		return ret;
	}
    public void insertTrialMeta(TrialMeta trialMeta) {
        System.out.println(insertTrialMeta);
        jdbcTemplate.update(insertTrialMeta,  trialMeta.getOS(), trialMeta.getName());
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
    
    public List<?> adHocQuery(String sql, RowMapper<?> rowMapper, Map<String, Object> args) {
        return jdbcTemplate.query(sql, rowMapper, args);
    }

    public List<?> adHocQuery(String sql, RowMapper<?> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

	public static BaseJdbcDao createJdbcDao(Properties properties) {
	    final BaseJdbcDao jdbcDao;
	    switch(Tiro.getPlatform()) {
	    case OSX:
	        jdbcDao = new OsxJdbcDao(properties);
	        break;
	    case Linux:
	    	jdbcDao = new LinuxJdbcDao(properties);
	    	break;
	    case Windows:
	    default:
	        throw new RuntimeException("unsupported platform");
	    }
		return jdbcDao;
	}
}
