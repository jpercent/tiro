package syndeticlogic.tiro.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import syndeticlogic.tiro.stat.OsxAggregatedIOStats;
import syndeticlogic.tiro.stat.OsxCpuStats;
import syndeticlogic.tiro.stat.OsxIOStats;
import syndeticlogic.tiro.stat.OsxMemoryStats;

import java.sql.PreparedStatement;

public class OsxJdbcDao extends BaseJdbcDao {
   
    public OsxJdbcDao(Properties config) {
    	super(config);
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
    
    public void completeTrial(OsxAggregatedIOStats ioStats, OsxMemoryStats memoryStats, OsxCpuStats cpuStats, long duration, long trialId) {
        jdbcTemplate.update(completeTrial, duration, ioStats.getAverageMegabytesPerSecond(), cpuStats.getAverageUserModeTime(), 
                cpuStats.getAverageSystemModeTime(), cpuStats.getAverageIdleModeTime(), memoryStats.getAverageFreePages(), 
                memoryStats.getAverageActivePages(), memoryStats.getAverageInactivePages(), memoryStats.getAverageWiredPages(), 
                memoryStats.getAverageNumberOfFaultRoutineCalls(), memoryStats.getAverageCopyOnWriteFaults(), memoryStats.getAverageZeroFilledPages(),
                memoryStats.getAveragePageIns(), memoryStats.getAveragePageOuts(), trialId);
    }
}
