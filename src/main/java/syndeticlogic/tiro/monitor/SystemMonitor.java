package syndeticlogic.tiro.monitor;

import java.io.BufferedReader;
import java.io.IOException;

import syndeticlogic.tiro.persistence.IOStats;
import syndeticlogic.tiro.persistence.MemoryStats;

public class SystemMonitor extends AbstractMonitor implements MemoryMonitor, IOMonitor {
	private MemoryMonitor memoryMonitor;
	private IOMonitor ioMonitor;
	private enum State { Created, Started, Finished };
	private State state = State.Created;
	
	public static SystemMonitor createSystemMonitor(Monitor.Platform platform, String device) {
	    final SystemMonitor monitor;
	    switch(platform) {
	    case OSX:
	        monitor = new SystemMonitor(new OSXMemoryMonitor(), new OSXIOMonitor(device));
	        break;
	    case Linux:
	    case Windows:
	    default:
	        throw new RuntimeException("unsupported platform");
	    }
	    return monitor;
	}
	
	public SystemMonitor(MemoryMonitor memoryMonitor, IOMonitor ioMonitor) {
	    super();
	    this.memoryMonitor = memoryMonitor;
	    this.ioMonitor = ioMonitor;
	}
	
    @Override
	public void start() {
        assert state == State.Created;
	    memoryMonitor.start();
	    ioMonitor.start();
	    state = State.Started;
	}
    @Override    	
	public void finish() {
        assert state == State.Started;
	    memoryMonitor.finish();
	    ioMonitor.finish();
	    state = State.Finished;
	}
    @Override   
    public void dumpData() {
        assert state == State.Finished;
        memoryMonitor.dumpData();
        ioMonitor.dumpData();
    }
    @Override
    protected void processMonitorOutput(BufferedReader reader) throws IOException {
        throw new RuntimeException("unsupported");
    }
    @Override
    public IOStats getIOStats() {
        return ioMonitor.getIOStats();
    }
    @Override
    public MemoryStats getMemoryStats() {
        return memoryMonitor.getMemoryStats();
    }
    @Override
    public String getDevice() {
        return ioMonitor.getDevice();
    }
    @Override
    public long getStart() {
        assert state.ordinal() >= State.Started.ordinal();
        return ioMonitor.getStart();
    }
    @Override
    public long getFinish() {
        assert state == State.Finished;
        return ioMonitor.getFinish();
    }
    @Override
    public long getDurationMillis() {
        assert state == State.Finished;
        return ioMonitor.getDurationMillis();
    }
}