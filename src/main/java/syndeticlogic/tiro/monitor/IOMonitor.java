package syndeticlogic.tiro.monitor;

import syndeticlogic.tiro.persistence.CpuStats;
import syndeticlogic.tiro.persistence.OSXIOStats;

public interface IOMonitor extends Monitor {
    OSXIOStats[] getIOStats();
    String[] getDevices();
    CpuStats getCpuStats();
}
