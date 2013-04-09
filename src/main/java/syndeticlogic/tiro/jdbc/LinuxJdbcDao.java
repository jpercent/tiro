package syndeticlogic.tiro.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import syndeticlogic.tiro.stat.LinuxAggregatedIOStats;
import syndeticlogic.tiro.stat.LinuxCpuStats;
import syndeticlogic.tiro.stat.LinuxIOStats;
import syndeticlogic.tiro.stat.LinuxMemoryStats;

import java.sql.PreparedStatement;

public class LinuxJdbcDao extends BaseJdbcDao {
   
    public LinuxJdbcDao(Properties config) {
    	super(config);
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

	public void completeTrial(LinuxAggregatedIOStats aggregatedIOStats, LinuxMemoryStats memoryStats, LinuxCpuStats cpuStats, long duration, Long trialId) {
        jdbcTemplate.update(completeTrial, duration, aggregatedIOStats.getAverageKpsRead(), cpuStats.getAverageUserModeTime(), 
                cpuStats.getAverageSystemModeTime(), cpuStats.getAverageIowaitTime(), cpuStats.getAverageIdleModeTime(),                 
                memoryStats.getFree(), memoryStats.getBuffers(), memoryStats.getCached(), memoryStats.getSwapCached(), memoryStats.getActive(), memoryStats.getActiveAnon(),
            	memoryStats.getActiveFile(), memoryStats.getInactive(), memoryStats.getInactiveAnon(), memoryStats.getInactiveFile(), memoryStats.getUnevictable(),
            	memoryStats.getSwapTotal(), memoryStats.getSwapFree(), memoryStats.getDirty(), memoryStats.getWriteback(), memoryStats.getAnon(), memoryStats.getSlab(),
            	memoryStats.getSreclaim(), memoryStats.getSunreclaim(), memoryStats.getKernelStack(), memoryStats.getBounce(), memoryStats.getVmallocTotal(), 
            	memoryStats.getVmallocUsed(), memoryStats.getVmallocChunk(), trialId);
	}
}
