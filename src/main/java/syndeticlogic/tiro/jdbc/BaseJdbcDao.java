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
    protected SQLStatements sql;
    
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
        
        this.sql = new SQLStatements(config);
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
        if(id != -1) id++;
        else id = 1;
        return id;
    }
    
    public void initialize() {
    	if(needsInit()) {
    		createTables();
    	}
        synchronized(this) {
        	trialsId = getId(sql.getSelectTrialsLast());
            controllersId = getId(sql.getSelectControllersLast());
            ioRecordsId = getId(sql.getSelectIORecordsLast());
            ioStatsId = getId(sql.getSelectIOStatsLast());
            memoryStatsId = getId(sql.getSelectMemoryStatsLast());
            cpuStatsId = getId(sql.getSelectCpuStatsLast());
            aggregateStatsId = getId(sql.getSelectAggregateStatsLast());
        }
    }
    
    public void createTables() {
        jdbcTemplate.execute(sql.getCreateTrialsMeta());
        jdbcTemplate.execute(sql.getCreateControllersMeta());
        jdbcTemplate.execute(sql.getCreateTrials());
        jdbcTemplate.execute(sql.getCreateControllers());
        jdbcTemplate.execute(sql.getCreateIORecords());
        jdbcTemplate.execute(sql.getCreateIOStats());
        jdbcTemplate.execute(sql.getCreateMemoryStats());
        jdbcTemplate.execute(sql.getCreateCpuStats());
        jdbcTemplate.execute(sql.getCreateAggregateStats());
    }
    
	public boolean needsInit() {
		boolean ret = false;
		Connection connection=null;
		try {
			connection = source.getConnection();
			DatabaseMetaData dbm = connection.getMetaData();
			
			@SuppressWarnings("serial")
			LinkedList<String> tables = new LinkedList<String>() {{
				add(sql.getTrialsMeta()); add(sql.getControllersMeta()); add(sql.getTrials());
				add(sql.getControllers()); add(sql.getIoRecords()); add(sql.getMemoryStats());
				add(sql.getIoStats()); add(sql.getAggregateStats());
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
    	synchronized(this) {
    		jdbcTemplate.update(sql.getInsertTrialMeta(),  trialMeta.getOS(), trialMeta.getName());
    		trialMeta.setId(jdbcTemplate.queryForLong(sql.getSelectTrialsMetaLast()));
    	}
    }

    public void insertControllerMeta(ControllerMeta controllerMeta) {
    	synchronized(this) {
    		jdbcTemplate.update(sql.getInsertControllerMeta(), controllerMeta.getControllerTypeName(), controllerMeta.getExecutorTypeName(), 
    				controllerMeta.getMemoryTypeName(), controllerMeta.getDevice());        
    		controllerMeta.setId(jdbcTemplate.queryForLong(sql.getSelectControllersMetaLast()));
    	}
    }
    
    public void insertTrial(Trial trial) {
        synchronized(this) {
            trial.setId(trialsId++);
        }
        jdbcTemplate.update(sql.getInsertTrial(), trial.getId() , trial.getMeta().getId());
    }
    
    public void insertController(Controller controller) { 
        synchronized(this) {
            controller.setId(controllersId++);
        }
        jdbcTemplate.update(sql.getInsertController(), controller.getId(), controller.getTrialMeta().getId(), controller.getControllerMeta().getId());
    }
    
    public void insertControllers(final Controller[] controllers) {
        final long id;
        synchronized(this) {
            id = controllersId;
            controllersId += controllers.length;
        }
        jdbcTemplate.batchUpdate(sql.getInsertController(), new BatchPreparedStatementSetter() {
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
        jdbcTemplate.update(sql.getCompleteController(), duration);
    }
    
    public void insertIORecord(final List<IORecord> records) {
        final long id;
        synchronized(this) {
            id = ioRecordsId;
            ioRecordsId += records.size();
        }
        jdbcTemplate.batchUpdate(sql.getInsertIORecord(), new BatchPreparedStatementSetter() {
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
