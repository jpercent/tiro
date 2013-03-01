package syndeticlogic.tiro.monitor;

public interface IOMonitor extends Monitor {
    String getDevice();
    IOStats getIOStats();
}
