package syndeticlogic.tiro.monitor;

import syndeticlogic.tiro.persistence.IOStats;

public interface IOMonitor extends Monitor {
    String getDevice();
    IOStats getIOStats();
}
