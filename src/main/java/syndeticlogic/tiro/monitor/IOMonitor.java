package syndeticlogic.tiro.monitor;

import syndeticlogic.tiro.stat.CpuStats;
import syndeticlogic.tiro.stat.IOStats;

public interface IOMonitor extends Monitor {
    IOStats[] getIOStats();
    String[] getDevices();
    CpuStats getCpuStats();
}
