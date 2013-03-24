package syndeticlogic.tiro.monitor;

import syndeticlogic.tiro.persistence.stats.MemoryStats;

public interface MemoryMonitor extends Monitor {
    MemoryStats getMemoryStats();
}
