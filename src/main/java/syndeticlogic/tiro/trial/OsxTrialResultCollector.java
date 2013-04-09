package syndeticlogic.tiro.trial;

import java.util.HashMap;

import syndeticlogic.tiro.jdbc.BaseJdbcDao;
import syndeticlogic.tiro.jdbc.OsxJdbcDao;
import syndeticlogic.tiro.monitor.SystemMonitor;

import syndeticlogic.tiro.stat.OsxAggregatedIOStats;
import syndeticlogic.tiro.stat.OsxCpuStats;
import syndeticlogic.tiro.stat.OsxIOStats;
import syndeticlogic.tiro.stat.OsxMemoryStats;

public class OsxTrialResultCollector extends TrialResultCollector {
	private final OsxJdbcDao osxJdbcDao;
	
    public OsxTrialResultCollector(OsxJdbcDao jdbcDaoa) {
    	super((BaseJdbcDao)jdbcDaoa);
        this.osxJdbcDao = jdbcDaoa;
    }

    public void completeTrial(Long trialId, SystemMonitor monitor, long duration) {
    	completeTrial(trialId, (OsxMemoryStats)monitor.getMemoryStats(), (OsxIOStats[])monitor.getIOStats(), (OsxCpuStats)monitor.getCpuStats(), duration);

    }

    private void completeTrial(Long trialId, OsxMemoryStats memoryStats, OsxIOStats[] ioStats, OsxCpuStats cpuStats, long duration) {
        HashMap<String, OsxIOStats> iostatsByDevice = new HashMap<String, OsxIOStats>();
        for(OsxIOStats iostat : ioStats) {
        	osxJdbcDao.insertIOStats(iostat, trialId);
            iostatsByDevice.put(iostat.getDevice(), iostat);
        }
        osxJdbcDao.insertMemoryStats(memoryStats, trialId);
        osxJdbcDao.insertCpuStats(cpuStats, trialId);
        OsxAggregatedIOStats aggregatedIOStats = new OsxAggregatedIOStats(iostatsByDevice);
        osxJdbcDao.completeTrial(aggregatedIOStats, memoryStats, cpuStats, duration, trialId);
        done = true;
        condition.signalAll();
	}
}
