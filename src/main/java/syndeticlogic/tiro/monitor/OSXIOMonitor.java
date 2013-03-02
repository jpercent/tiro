package syndeticlogic.tiro.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import syndeticlogic.tiro.persistence.IOStats;

public class OSXIOMonitor extends AbstractMonitor implements IOMonitor {
    private static final Log log = LogFactory.getLog(OSXIOMonitor.class);
    private IOStats iostats;
    private String device;

	public OSXIOMonitor(String device) {
		super();
		setCommandAndArgs("iostat", device, "5");
		iostats = new IOStats();
	}
	@Override
	protected void processMonitorOutput(BufferedReader reader) throws IOException {
		reader.readLine();
		reader.readLine();
		reader.readLine();
		while(true) {
			String line = reader.readLine();
			if(line == null) {
				break;
			}
			log.info(line);
			line = line.trim();
			String[] values = line.split("\\s+");
			assert values.length == 9;
			int i = 0;
			Double kbt = Double.parseDouble(values[i++]);
			Double tps = Double.parseDouble(values[i++]);
			Double mbs = Double.parseDouble(values[i++]);		
			Long user = Long.parseLong(values[i++]);
			Long system = Long.parseLong(values[i++]);
			Long idle = Long.parseLong(values[i++]);
			iostats.addRawRecord(kbt, tps, mbs, user, system, idle);
		}
	}
    @Override
    public void dumpData() {
        iostats.dumpData();
    }
    @Override
    public IOStats getIOStats() {
        return iostats;
    }
    @Override
    public String getDevice() {
        return device;
    }
    
	public static void useDisk() throws IOException {
		File file = new File("iomonitor.perf");
		FileOutputStream out = new FileOutputStream(file);
		byte[] bytes = new byte[1024*1024*10];
		for(int j = 0; j < 1024*1024*10; j++) {
			bytes[j] = (byte)(23 * j);
		}
		out.write(bytes);
		out.close();
		assert file.delete();
	}
	
	public static void useCpu() {
		Random r = new Random();
		long first = r.nextLong();
		int secondBound = (int)first;
		if(secondBound < 0) {
			secondBound = -secondBound;
		}
		long second = r.nextInt(secondBound);
		long rem = -1;
		while (rem != 0) {
			rem = first % second;
			first = second;
			second = rem;
		}
	}
	
	public static void main(String[] args) throws Throwable {
		try {
			long starttime = System.currentTimeMillis();
			OSXIOMonitor iom = new OSXIOMonitor("disk0");
			System.out.println("Starting..");
			iom.start();
			Thread.sleep(1000);
			if (args.length == 0) {
				while (starttime + 25522 > System.currentTimeMillis()) {
					useDisk();
				}
			} else {
				while (starttime + 25522 > System.currentTimeMillis()) {
					useCpu();
				}
			}
			iom.finish();
			iom.dumpData();
			long duration = iom.getDurationMillis();
			System.out.println("Duration = " + duration);
		} catch (Throwable t) {
			log.error("exception: ", t);
			throw t;
		}
	}
}


