package syndeticlogic.tiro.monitor;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import syndeticlogic.tiro.persistence.stats.LinuxMemoryStats;
import syndeticlogic.tiro.persistence.stats.MemoryStats;

public class LinuxMemoryMonitor extends AbstractMonitor implements MemoryMonitor {
	private static final Log log = LogFactory.getLog(LinuxMemoryMonitor.class);
	private final LinuxMemoryStats memoryStats;

	public LinuxMemoryMonitor() {
		super();
		String command = "./proc-meminfo.bsh";//while [ true ] \ndo \ncat /proc/meminfo && sleep 5 \ndone";
		
		setCommandAndArgs(command);
		memoryStats = new LinuxMemoryStats();
	}
	
	protected String[] getNextRecord(BufferedReader reader) throws IOException {
		String[] attributes = new String[42];
		for(int i = 0; i < attributes.length; i++) {
			String line = reader.readLine();
			if (line == null) {
				assert i == 0;
				return null;
			}
			
			log.debug(line);
			line = line.trim();
			String[] values = line.split("\\s+");
			attributes[i] = values[1];
			System.out.println(i+"th attribute = "+attributes[i]);
			System.out.println("values.length = "+values.length);
			assert values.length == 11;
		}
		return attributes;
	}
	
	@Override
	protected void processMonitorOutput(BufferedReader reader) throws IOException {
		while (true) {
			int i = 0;
			String[] values = getNextRecord(reader);
			if (values == null) {
				return;
			}
			i++; // skip mem total
			Long free = Long.parseLong(values[i++]);//.split('\n')[1]);
			Long buffers = Long.parseLong(values[i++]);
			Long cached = Long.parseLong(values[i++]);
			Long swapCached = Long.parseLong(values[i++]);
            Long active = Long.parseLong(values[i++]);
            Long inactive = Long.parseLong(values[i++]);
            Long activeAnon = Long.parseLong(values[i++]);
            Long inactiveAnon = Long.parseLong(values[i++]);
            Long activeFile = Long.parseLong(values[i++]);
            Long inactiveFile = Long.parseLong(values[i++]);
            Long unevictable = Long.parseLong(values[i++]);
            i++; // skip mlocked
            Long swapTotal = Long.parseLong(values[i++]);
            Long swapFree = Long.parseLong(values[i++]);
            Long dirty = Long.parseLong(values[i++]);
            Long writeback = Long.parseLong(values[i++]);
            Long anon = Long.parseLong(values[i++]);
            i++; // skip mapped
            i++; // skip shem
            Long slab = Long.parseLong(values[i++]);
            Long sreclaim = Long.parseLong(values[i++]);
            Long sunreclaim = Long.parseLong(values[i++]);
            Long kernelStack = Long.parseLong(values[i++]);
            i++; // skip page tables
            i++; // skip nfs unstable
            Long bounce = Long.parseLong(values[i++]);
            i++; // skip writeback tmp
            i++; // skip commit limit
            i++; // skip committed as
            Long vmallocTotal = Long.parseLong(values[i++]);
            Long vmallocUsed = Long.parseLong(values[i++]);
            Long vmallocChunk = Long.parseLong(values[i++]);
            i++; // hardware corrupted
            i++; // anon huge pages
            i++; // huge pages total
            i++; // huge pages free
            i++; // huge pages rsvd
            i++; // huge pages surp
            i++; // huge pages size
            i++; // direct map 4k
            i++; // direct map 2m
			memoryStats.addRawRecord(free, buffers, cached, swapCached, active, activeAnon, activeFile, 
					inactive, inactiveAnon, inactiveFile, unevictable, swapTotal, swapFree, dirty, writeback, anon, 
					slab, sreclaim,  sunreclaim,  kernelStack,  bounce,  vmallocTotal, vmallocUsed,  vmallocChunk);
        }
	}
	@Override
	public MemoryStats getMemoryStats() {
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
			LinuxMemoryMonitor mm = new LinuxMemoryMonitor();
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
