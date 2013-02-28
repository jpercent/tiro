package syndeticlogic.tiro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.PreparedStatement;

public class TrialResultsJdbcDao {
    private final RowMapper<TrialResult> rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final String driverClassName;
    private final String jdbcUrl;
    
    private final String createTrialsMeta;
    private final String createControllersMeta;
    private final String createTrials;
    private final String createControllers;
    private final String createIORecords;
    private final String createIOStats;
    private final String createMemoryStats;
    
    private final String insertTrialMeta;
    private final String insertControllerMeta;
    private final String insertTrial;
    private final String insertController;
    private final String insertIORecord;
    private final String insertIOStats;
    private final String insertMemoryStats;
    
    private final String selectTrialsMetaLast;
    private final String selectControllersMetaLast;
    private final String selectTrialsLast;
    private final String selectControllersLast;
    private final String selectIORecordsLast;
    private final String selectIOStatsLast;
    private final String selectMemoryStatsLast;
    
    private final String completeTrial;
    private final String completeController;
    
    private long trialsId;
    private long controllersId;
    private long ioRecordsId;
    private long ioStatsId;
    private long memoryStatsId;
   
    // derby driverclassname == org.apache.derby.jdbc.EmbeddedDriver
    // derby jdbcUrl == jdbc:derby://localhost:21529/tmp/catena/trial_results;create=true
    // postgres driverClassName == org.postgresql.Driver
    // postres jdbcUrl == jdbc:postgresql://localhost:5432/catena?user=james&password=password
    // sqlite driverClassName == org.sqlite.JDBC
    // sqlite jdburl == jdbc:sqlite:trial-result.db;foreign keys=true?user=james&password=password
    public TrialResultsJdbcDao(Properties config) {
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
        assert createTrialsMeta != null && createTrials != null && createControllersMeta !=null && createControllers != null 
                && createIORecords != null && createIOStats != null && createMemoryStats != null;
        
        this.insertTrialMeta = config.getProperty("insert-trial-meta"); 
        this.insertControllerMeta = config.getProperty("insert-controller-meta"); 
        this.insertTrial = config.getProperty("insert-trial"); 
        this.insertController = config.getProperty("insert-controller"); 
        this.insertIORecord = config.getProperty("insert-iorecord");
        this.insertIOStats = config.getProperty("insert-io-stats");
        this.insertMemoryStats = config.getProperty("insert-memory-stats");
        assert insertTrialMeta != null && insertControllerMeta != null && insertTrial != null && insertController != null
                && insertIORecord != null && insertMemoryStats != null && insertIOStats != null;
        
        this.selectTrialsMetaLast = config.getProperty("select-trials-meta-last-id");
        this.selectControllersMetaLast = config.getProperty("select-controllers-meta-last-id");
        this.selectTrialsLast = config.getProperty("select-trials-last-id");
        this.selectControllersLast = config.getProperty("select-controllers-last-id");
        this.selectIORecordsLast = config.getProperty("select-iorecords-last-id");
        this.selectIOStatsLast = config.getProperty("select-io-stats-last-id");
        this.selectMemoryStatsLast = config.getProperty("select-memory-stats-last-id");
        assert selectTrialsMetaLast != null && selectControllersMetaLast != null && selectTrialsLast != null 
                && selectControllersLast != null && selectIORecordsLast != null && selectIOStatsLast != null 
                && selectMemoryStatsLast != null;
        
        this.completeTrial = config.getProperty("complete-trial");
        this.completeController = config.getProperty("complete-controller");
        assert completeTrial != null && completeController != null; 
        
        boolean createTables = Boolean.parseBoolean(config.getProperty("create-tables"));
        System.out.println("table s= value = "+"true".equals(config.getProperty("create-tables"))+createTables);
        
        // create data source connection
        DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName(driverClassName);
        source.setUrl(jdbcUrl);
        jdbcTemplate = new JdbcTemplate(source);
        rowMapper = new TrialResultMapper();
        trialsId = 0;
        controllersId = 0;
        ioRecordsId = 0;
        ioStatsId = 0;
        memoryStatsId = 0;
    }
    
    private long getId(String query) {
        long id = -1;
        try {
            id = jdbcTemplate.queryForObject(query, Long.class);
        } catch (EmptyResultDataAccessException e) {
        }
        if(id != -1) id++;
        else id = 0;
        return id;
    }
    
    public void initialize() {
        controllersId = getId(selectControllersLast);
        ioRecordsId = getId(selectIORecordsLast);
        ioStatsId = getId(selectIOStatsLast);
        memoryStatsId = getId(selectMemoryStatsLast);
    }
    
    public void createTables() {
        jdbcTemplate.execute(createTrialsMeta);
        jdbcTemplate.execute(createControllersMeta);
        jdbcTemplate.execute(createTrials);
        jdbcTemplate.execute(createControllers);
        jdbcTemplate.execute(createIORecords);
        jdbcTemplate.execute(createIOStats);
        jdbcTemplate.execute(createMemoryStats);
    }
    
    public void insertTrialMeta(TrialMeta trialMeta) {
        jdbcTemplate.update(insertTrialMeta, trialMeta.getName());
        trialMeta.setId(jdbcTemplate.queryForLong(selectTrialsMetaLast));
    }

    public void insertControllerMeta(ControllerMeta controllerMeta) {
        jdbcTemplate.update(insertControllerMeta, controllerMeta.getControllerTypeName(), controllerMeta.getExecutorTypeName(), 
                controllerMeta.getMemoryTypeName(), controllerMeta.getDevice());
        controllerMeta.setId(jdbcTemplate.queryForLong(selectControllersMetaLast));
    }
    
    public void insertTrial(TrialMeta meta) {
        jdbcTemplate.update(insertTrial, trialsId++, meta.getId());
    }
    
    public void completeTrial(IOMonitor iom, MemoryMonitor mm, long duration, long trialId) {
        jdbcTemplate.update(completeTrial, duration, iom.getAverageMegabytesPerSecond(), iom.getAverageUserModeTime(), 
                iom.getAverageSystemModeTime(), iom.getAverageSystemModeTime(), mm.getAverageFreePages(), 
                mm.getAverageActivePages(), mm.getAverageInactivePages(), mm.getAverageWiredPages(), 
                mm.getAverageNumberOfFaultRoutineCalls(), mm.getAverageCopyOnWriteFaults(), mm.getAverageZeroFilledPages(),
                mm.getAveragePageIns(), mm.getAveragePageOuts(), trialId);
    }
            
    public void insertController(TrialMeta tmeta, ControllerMeta cmeta) {
        jdbcTemplate.update(insertController, controllersId++, tmeta.getId(), cmeta.getId());
    }
    
    public void completeController(long controllerId, long duration) {
        jdbcTemplate.update(completeController, duration);
    }
    
    public void insertIORecord(final IORecord[] records) {
        final long id = ioRecordsId;
        ioRecordsId += records.length;
        jdbcTemplate.batchUpdate(insertIORecord, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id+i);
                ps.setLong(2, records[i].getControllerId());
                ps.setLong(3, records[i].getLba());
                ps.setLong(4, records[i].getDuration());
                ps.setInt(5, records[i].getSize());
                ps.setString(6, records[i].getStrategyName());
            }
            @Override
            public int getBatchSize() {
                return records.length;
            }
        });
    }
    
    public void insertIOStats(IOMonitor monitor, final long trialId) {
        final List<Double> kilobytesPerTransfer = monitor.getRawKiloBytesPerTranferMeasurements();
        final List<Double> transfersPerSecond = monitor.getRawTransfersPerSecond();
        final List<Double> megabytesPerSecond = monitor.getRawMegabytesPerSecond();
        final List<Long> userMode = monitor.getRawUserModeTime();
        final List<Long> systemMode = monitor.getRawSystemModeTime();
        final List<Long> idleMode = monitor.getRawIdleModeTime();
        final long id = ioStatsId;
        ioStatsId += idleMode.size();
        jdbcTemplate.batchUpdate(insertIOStats, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id+i);
                ps.setLong(2, trialId);
                ps.setDouble(3, kilobytesPerTransfer.get(i));
                ps.setDouble(4, transfersPerSecond.get(i));
                ps.setDouble(5, megabytesPerSecond.get(i));
                ps.setLong(6, userMode.get(i));
                ps.setLong(7, systemMode.get(i));
                ps.setLong(8, idleMode.get(i));
            }
            @Override
            public int getBatchSize() {
                return idleMode.size();
            }
        });
    }
    
    public void insertMemoryStats(MemoryMonitor monitor, final long trialId) {
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
        final long id = memoryStatsId;
        memoryStatsId += freePages.size();
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
    
    public List<TrialResult> adHocQuery(String sql, Map<String, Object> args) {
        return jdbcTemplate.query(sql, rowMapper, args);
    }
    
    private class TrialResultMapper implements RowMapper<TrialResult> {
        @Override
        public TrialResult mapRow(ResultSet rs, int rowNum) throws SQLException {
            TrialResult t = new TrialResult();
            t.setDuration(rs.getLong("duration"));
            return t;
        }
    }
}
