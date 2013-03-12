package syndeticlogic.tiro.monitor;

import syndeticlogic.tiro.persistence.OSXMemoryStats;

public interface MemoryMonitor extends Monitor {
    OSXMemoryStats getMemoryStats();
}
