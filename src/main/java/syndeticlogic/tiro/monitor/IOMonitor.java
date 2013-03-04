package syndeticlogic.tiro.monitor;

import syndeticlogic.tiro.persistence.CpuStats;
import syndeticlogic.tiro.persistence.IOStats;

public interface IOMonitor extends Monitor {
    IOStats[] getIOStats();
    String[] getDevices();
    CpuStats getCpuStats();
}
