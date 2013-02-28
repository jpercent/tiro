package syndeticlogic.tiro;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

public class BufferPerformanceTest {
	public enum Algorithm { LRU, FRU, MRU, LRUK, Postgres};
	
	public class Stats {

		Algorithm algorithm;
		int disks;
		int threads;
		long poolSize;
		volatile long startTime;
		volatile long endTime;
		volatile long ioBytes;
		
		public Stats(Algorithm algo, int disks, int threads, int poolSize) {
			this.algorithm = algo;
			this.disks = disks;
			this.threads = threads;
			this.poolSize = poolSize;
		}
		
		public void start() {
			startTime = System.nanoTime();
		}
		
		public void stop() {
			endTime = System.nanoTime();
		}
		
		public void spend(long iobytes) {
			this.ioBytes += iobytes;
		}
		
		public long duration() {
			return endTime - startTime; 
		}
		
		public long MBPerSecond() {
			return (ioBytes/1000000000)/duration();
		}
		public ConcurrentLinkedQueue<Stats> results = new ConcurrentLinkedQueue<Stats>();
	}
	
	@Test
	public void bfsTest() {
		// do bfsTest config
	//	runAllTests();
	}
	/*
	public runAllTests() {
		for paralleldisk in numDisk {
			for bufferPoolAlgorthim in alorithms {	
				for bufferPoolSizes in poolsizes {
					for threads in 2*cpus {
						record(run(readSequnta1thread));
						record(writesequential1thread));
						record(randomReadWrite));
					}
				}
			}
		}
	}
	
	
	@Test
	public void ext4Test() {
	}
	
	@Test
	public void blockTest() {
	}
	
	public Stats readSequentialBandwidthTest(Stats stats) {

	}
	
	public void writeSequentialBandwidthTest() {
		
	}
	
	public void randomReadWriteBandwidthTest() {
		
	}
	*/
	
}
