package syndeticlogic.tiro.monitor;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import syndeticlogic.tiro.persistence.OSXMemoryStats;

public class OSXMemoryMonitor extends AbstractMonitor implements MemoryMonitor {
	private static final Log log = LogFactory.getLog(OSXMemoryMonitor.class);
	private final OSXMemoryStats memoryStats;

	public OSXMemoryMonitor() {
		super();
		setCommandAndArgs("vm_stat", "5");
		memoryStats = new OSXMemoryStats();
	}
	@Override
	protected void processMonitorOutput(BufferedReader reader) throws IOException {
		reader.readLine();
		reader.readLine();
		reader.readLine();
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			log.debug(line);
			line = line.trim();
			String[] values = line.split("\\s+");
			assert values.length == 11;
			int i = 0;
			Long free = Long.parseLong(values[i++]);
			Long active = Long.parseLong(values[i++]);
			Long speculative = Long.parseLong(values[i++]);
			Long inactive = Long.parseLong(values[i++]);
            Long wired = Long.parseLong(values[i++]);
            Long faults = Long.parseLong(values[i++]);
            Long copyOnWriteFaults = Long.parseLong(values[i++]);
            Long zeroFilled = Long.parseLong(values[i++]);
            Long reactive = Long.parseLong(values[i++]);
            Long pageIns = Long.parseLong(values[i++]);
            Long pageOuts = Long.parseLong(values[i++]);

			memoryStats.addRawRecord(free, active, speculative, inactive, wired, faults, copyOnWriteFaults, zeroFilled,
			        reactive, pageIns, pageOuts);
        }
	}
	@Override
	public OSXMemoryStats getMemoryStats() {
	    return memoryStats;
	}
	@Override
	public void dumpData() {
	    memoryStats.dumpData();
	}
	
	public static void useMemory() {
		byte[] bytes = new byte[1024*1024*100];
		for(int j = 0; j < 1024*1024*100; j++) {
			bytes[j] = (byte)(23 * j);
		}
	}
	
	public static void main(String[] args) throws Throwable {
		try {
			long starttime = System.currentTimeMillis();
			OSXMemoryMonitor mm = new OSXMemoryMonitor();
			System.out.println("Starting..");
			mm.start();
			Thread.sleep(1000);
			if (args.length == 0) {
				while (starttime + 25522 > System.currentTimeMillis()) {
					useMemory();
				}
			} else {
				while (starttime + 25522 > System.currentTimeMillis()) {
					long count1 = 0;
					while (count1 < 10000000000L)
						count1++;
				}
			}
			mm.finish();
			mm.dumpData();
			long duration = mm.getDurationMillis();
			System.out.println("Duration = " + duration);
		} catch (Throwable t) {
			log.error("exception: ", t);
			throw t;
		}
	}
} 
