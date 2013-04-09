package syndeticlogic.tiro.monitor;

import syndeticlogic.tiro.stat.MemoryStats;

public interface MemoryMonitor extends Monitor {
    MemoryStats getMemoryStats();
}
