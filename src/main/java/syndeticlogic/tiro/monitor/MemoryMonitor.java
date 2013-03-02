package syndeticlogic.tiro.monitor;

import syndeticlogic.tiro.persistence.MemoryStats;

public interface MemoryMonitor extends Monitor {
    MemoryStats getMemoryStats();
}
