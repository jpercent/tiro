package syndeticlogic.tiro.monitor;

import syndeticlogic.tiro.persistence.stats.CpuStats;
import syndeticlogic.tiro.persistence.stats.IOStats;

public interface IOMonitor extends Monitor {
    IOStats[] getIOStats();
    String[] getDevices();
    CpuStats getCpuStats();
}
