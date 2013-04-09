package syndeticlogic.tiro.trial;

import java.util.HashMap;

import syndeticlogic.tiro.jdbc.LinuxJdbcDao;
import syndeticlogic.tiro.monitor.SystemMonitor;
import syndeticlogic.tiro.stat.LinuxAggregatedIOStats;
import syndeticlogic.tiro.stat.LinuxCpuStats;
import syndeticlogic.tiro.stat.LinuxIOStats;
import syndeticlogic.tiro.stat.LinuxMemoryStats;

public class LinuxTrialResultCollector extends TrialResultCollector {
	private final LinuxJdbcDao linuxJdbcDao;
	
    public LinuxTrialResultCollector(LinuxJdbcDao jdbcDao) {
    	super(jdbcDao);
    	this.linuxJdbcDao = jdbcDao;
    }

    public void completeTrial(Long trialId, SystemMonitor monitor, long duration) {
    	completeTrial(trialId, (LinuxMemoryStats)monitor.getMemoryStats(), (LinuxIOStats[])monitor.getIOStats(), (LinuxCpuStats)monitor.getCpuStats(), duration);
    }
    
    private void completeTrial(Long trialId, LinuxMemoryStats memoryStats, LinuxIOStats[] ioStats, LinuxCpuStats cpuStats, long duration) {
        HashMap<String, LinuxIOStats> iostatsByDevice = new HashMap<String, LinuxIOStats>();
        for(LinuxIOStats iostat : ioStats) {
        	linuxJdbcDao.insertIOStats(iostat, trialId);
            iostatsByDevice.put(iostat.getDevice(), iostat);
        }
        linuxJdbcDao.insertMemoryStats(memoryStats, trialId);
        linuxJdbcDao.insertCpuStats(cpuStats, trialId);
        LinuxAggregatedIOStats aggregatedIOStats = new LinuxAggregatedIOStats(iostatsByDevice);
        linuxJdbcDao.completeTrial(aggregatedIOStats, memoryStats, cpuStats, duration, trialId);
        done = true;
        condition.signalAll();
	}
}
