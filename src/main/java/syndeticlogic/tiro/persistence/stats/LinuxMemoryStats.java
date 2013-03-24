package syndeticlogic.tiro.persistence.stats;

import java.util.LinkedList;

public class LinuxMemoryStats implements MemoryStats {
	
    private final LinkedList<Long> free;
    private final LinkedList<Long> buffers;
    private final LinkedList<Long> cached;
    private final LinkedList<Long> swapCached;
    private final LinkedList<Long> active;
    private final LinkedList<Long> activeAnon;
    private final LinkedList<Long> activeFile;
    private final LinkedList<Long> inactive;
    private final LinkedList<Long> inactiveAnon;
    private final LinkedList<Long> inactiveFile;
    private final LinkedList<Long> unevictable;
    private final LinkedList<Long> swapTotal;
    private final LinkedList<Long> swapFree;
    private final LinkedList<Long> dirty;
    private final LinkedList<Long> writeback;
    private final LinkedList<Long> anon;
    private final LinkedList<Long> slab;
    private final LinkedList<Long> sreclaim;
    private final LinkedList<Long> sunreclaim;
    private final LinkedList<Long> kernelStack;
    private final LinkedList<Long> bounce;
    private final LinkedList<Long> vmallocTotal;
    private final LinkedList<Long> vmallocUsed;
    private final LinkedList<Long> vmallocChunk;

	public LinuxMemoryStats() {
		free = new LinkedList<Long>();
		buffers = new LinkedList<Long>();
		cached = new LinkedList<Long>();
		swapCached = new LinkedList<Long>();
		active = new LinkedList<Long>();
		activeAnon = new LinkedList<Long>();
		activeFile = new LinkedList<Long>();
		inactive = new LinkedList<Long>();
		inactiveAnon = new LinkedList<Long>();
		inactiveFile = new LinkedList<Long>();
		unevictable = new LinkedList<Long>();
		swapTotal = new LinkedList<Long>();
		swapFree = new LinkedList<Long>();
		dirty = new LinkedList<Long>();
		writeback = new LinkedList<Long>();
		anon = new LinkedList<Long>();
		slab = new LinkedList<Long>();
		sreclaim = new LinkedList<Long>();
		sunreclaim = new LinkedList<Long>();
		kernelStack = new LinkedList<Long>();
		bounce = new LinkedList<Long>();
		vmallocTotal = new LinkedList<Long>();
		vmallocUsed = new LinkedList<Long>();
		vmallocChunk = new LinkedList<Long>();
	}
    
    public LinkedList<Long> getFree() {
		return free;
	}
    public double getAverageFree() {
        return Stats.computeAverage(free);
    }
    
	public LinkedList<Long> getBuffers() {
		return buffers;
	}
	public double getAverageBuffers() {
        return Stats.computeAverage(buffers);
    }
	public LinkedList<Long> getCached() {
		return cached;
	}
	public double getAverageCached() {
        return Stats.computeAverage(cached);
    }


	public LinkedList<Long> getSwapCached() {
		return swapCached;
	}
	public double getAverageSwapCached() {
        return Stats.computeAverage(swapCached);
    }

	public LinkedList<Long> getActive() {
		return active;
	}
	public double getAverageActive() {
        return Stats.computeAverage(active);
    }

	public LinkedList<Long> getActiveAnon() {
		return activeAnon;
	}
	public double getAverageActiveAnon() {
        return Stats.computeAverage(activeAnon);
    }

	public LinkedList<Long> getActiveFile() {
		return activeFile;
	}
	public double getAverageActiveFile() {
        return Stats.computeAverage(activeFile);
    }

	public LinkedList<Long> getInactive() {
		return inactive;
	}
	public double getAverageInactive() {
        return Stats.computeAverage(inactive);
    }

	public LinkedList<Long> getInactiveAnon() {
		return inactiveAnon;
	}
	public double getAverageInactiveAnon() {
        return Stats.computeAverage(inactiveAnon);
    }

	public LinkedList<Long> getInactiveFile() {
		return inactiveFile;
	}
	public double getAverageInactiveFile() {
        return Stats.computeAverage(inactiveFile);
    }

	public LinkedList<Long> getUnevictable() {
		return unevictable;
	}
	public double getAverageUnevictable() {
        return Stats.computeAverage(unevictable);
    }

	public LinkedList<Long> getSwapTotal() {
		return swapTotal;
	}
	public double getAverageSwapTotal() {
        return Stats.computeAverage(swapTotal);
    }

	public LinkedList<Long> getSwapFree() {
		return swapFree;
	}
	public double getAverageSwapFree() {
        return Stats.computeAverage(swapFree);
    }

	public LinkedList<Long> getDirty() {
		return dirty;
	}
	public double getAverageDirty() {
        return Stats.computeAverage(dirty);
    }

	public LinkedList<Long> getWriteback() {
		return writeback;
	}
	public double getAverageWriteback() {
        return Stats.computeAverage(writeback);
    }

	public LinkedList<Long> getAnon() {
		return anon;
	}
	public double getAverageAnon() {
        return Stats.computeAverage(anon);
    }

	public LinkedList<Long> getSlab() {
		return slab;
	}
	public double getAverageSlab() {
        return Stats.computeAverage(slab);
    }

	public LinkedList<Long> getSreclaim() {
		return sreclaim;
	}
	public double getAverageSreclaim() {
        return Stats.computeAverage(sreclaim);
    }

	public LinkedList<Long> getSunreclaim() {
		return sunreclaim;
	}
	public double getAverageSunrelaim() {
        return Stats.computeAverage(sunreclaim);
    }

	public LinkedList<Long> getKernelStack() {
		return kernelStack;
	}
	public double getAverageKernelStack() {
        return Stats.computeAverage(kernelStack);
    }

	public LinkedList<Long> getBounce() {
		return bounce;
	}
	public double getAverageBounce() {
        return Stats.computeAverage(bounce);
    }

	public LinkedList<Long> getVmallocTotal() {
		return vmallocTotal;
	}
	public double getAverageVmallocTotal() {
        return Stats.computeAverage(vmallocTotal);
    }

	public LinkedList<Long> getVmallocUsed() {
		return vmallocUsed;
	}
	public double getAverageVmallocUsed() {
        return Stats.computeAverage(vmallocUsed);
    }

	public LinkedList<Long> getVmallocChunk() {
		return vmallocChunk;
	}
	public double getAverageVmallocChunk() {
        return Stats.computeAverage(vmallocChunk);
        
    }

	public void addRawRecord(Long freeValue, Long buffersValue, Long cachedValue, Long swapCachedValue, Long activeValue, Long activeAnonValue,
							Long activeFileValue, Long inactiveValue, Long inactiveAnonValue, Long inactiveFileValue, Long unevictableValue,
							Long swapTotalValue, Long swapFreeValue, Long dirtyValue, Long writebackValue, Long anonValue, Long slabValue,
							Long sreclaimValue, Long sunreclaimValue, Long kernelStackValue, Long bounceValue, Long vmallocTotalValue,
							Long vmallocUsedValue, Long vmallocChunkValue) 
    {
		free.add(freeValue);
		buffers.add(buffersValue);
		cached.add(cachedValue);
		swapCached.add(swapCachedValue);
		active.add(activeValue);
		activeAnon.add(activeAnonValue);
		activeFile.add(activeFileValue);
		inactive.add(inactiveValue);
		inactiveAnon.add(inactiveAnonValue);
		inactiveFile.add(inactiveValue);
		unevictable.add(unevictableValue);
		swapTotal.add(swapTotalValue);
		swapFree.add(swapFreeValue);
		dirty.add(dirtyValue);
		writeback.add(writebackValue);
		anon.add(anonValue);
		slab.add(slabValue);
		sreclaim.add(sreclaimValue);
		sunreclaim.add(sunreclaimValue);
		kernelStack.add(kernelStackValue);
		bounce.add(bounceValue);
		vmallocTotal.add(vmallocTotalValue);
		vmallocUsed.add(vmallocUsedValue);
		vmallocChunk.add(vmallocChunkValue);
    }
    
    public void dumpData() {
        System.out.println("Free:                 "+free);
        System.out.println("Buffers:         "+buffers);
        System.out.println("Cached:          "+cached);
        System.out.println("SwapCached:       "+swapCached);
        System.out.println("Active:          "+active);
        System.out.println("ActiveAnon:  "+activeAnon);
        System.out.println("ActiveFile: "+activeFile);
        System.out.println("Inactive:          "+inactive);
        System.out.println("InactiveAnon:       "+inactiveAnon);
        System.out.println("InactiveFile:             "+inactiveFile);
        System.out.println("Unevictable:          "+unevictable);
        System.out.println("Swap Total:           "+swapTotal);
        System.out.println("Swap Free:            "+swapFree);
        System.out.println("Dirty :               "+dirty);
        System.out.println("Writeback:            "+writeback);
        System.out.println("Anon:                 "+anon);
        System.out.println("Slab:                 "+slab);
        System.out.println("Reclaimable:          "+sreclaim);
        System.out.println("Unreclaimable:        "+sunreclaim);
        System.out.println("Kernel Stack:         "+kernelStack);
        System.out.println("Bounce:               "+bounce);
        System.out.println("VMalloc Total:        "+vmallocTotal);
        System.out.println("VMalloc Used:         "+vmallocUsed);
        System.out.println("VMallocChunk:         "+vmallocChunk);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result
				+ ((activeAnon == null) ? 0 : activeAnon.hashCode());
		result = prime * result
				+ ((activeFile == null) ? 0 : activeFile.hashCode());
		result = prime * result + ((anon == null) ? 0 : anon.hashCode());
		result = prime * result + ((bounce == null) ? 0 : bounce.hashCode());
		result = prime * result + ((buffers == null) ? 0 : buffers.hashCode());
		result = prime * result + ((cached == null) ? 0 : cached.hashCode());
		result = prime * result + ((dirty == null) ? 0 : dirty.hashCode());
		result = prime * result + ((free == null) ? 0 : free.hashCode());
		result = prime * result
				+ ((inactive == null) ? 0 : inactive.hashCode());
		result = prime * result
				+ ((inactiveAnon == null) ? 0 : inactiveAnon.hashCode());
		result = prime * result
				+ ((inactiveFile == null) ? 0 : inactiveFile.hashCode());
		result = prime * result
				+ ((kernelStack == null) ? 0 : kernelStack.hashCode());
		result = prime * result + ((slab == null) ? 0 : slab.hashCode());
		result = prime * result
				+ ((sreclaim == null) ? 0 : sreclaim.hashCode());
		result = prime * result
				+ ((sunreclaim == null) ? 0 : sunreclaim.hashCode());
		result = prime * result
				+ ((swapCached == null) ? 0 : swapCached.hashCode());
		result = prime * result
				+ ((swapFree == null) ? 0 : swapFree.hashCode());
		result = prime * result
				+ ((swapTotal == null) ? 0 : swapTotal.hashCode());
		result = prime * result
				+ ((unevictable == null) ? 0 : unevictable.hashCode());
		result = prime * result
				+ ((vmallocChunk == null) ? 0 : vmallocChunk.hashCode());
		result = prime * result
				+ ((vmallocTotal == null) ? 0 : vmallocTotal.hashCode());
		result = prime * result
				+ ((vmallocUsed == null) ? 0 : vmallocUsed.hashCode());
		result = prime * result
				+ ((writeback == null) ? 0 : writeback.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinuxMemoryStats other = (LinuxMemoryStats) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (activeAnon == null) {
			if (other.activeAnon != null)
				return false;
		} else if (!activeAnon.equals(other.activeAnon))
			return false;
		if (activeFile == null) {
			if (other.activeFile != null)
				return false;
		} else if (!activeFile.equals(other.activeFile))
			return false;
		if (anon == null) {
			if (other.anon != null)
				return false;
		} else if (!anon.equals(other.anon))
			return false;
		if (bounce == null) {
			if (other.bounce != null)
				return false;
		} else if (!bounce.equals(other.bounce))
			return false;
		if (buffers == null) {
			if (other.buffers != null)
				return false;
		} else if (!buffers.equals(other.buffers))
			return false;
		if (cached == null) {
			if (other.cached != null)
				return false;
		} else if (!cached.equals(other.cached))
			return false;
		if (dirty == null) {
			if (other.dirty != null)
				return false;
		} else if (!dirty.equals(other.dirty))
			return false;
		if (free == null) {
			if (other.free != null)
				return false;
		} else if (!free.equals(other.free))
			return false;
		if (inactive == null) {
			if (other.inactive != null)
				return false;
		} else if (!inactive.equals(other.inactive))
			return false;
		if (inactiveAnon == null) {
			if (other.inactiveAnon != null)
				return false;
		} else if (!inactiveAnon.equals(other.inactiveAnon))
			return false;
		if (inactiveFile == null) {
			if (other.inactiveFile != null)
				return false;
		} else if (!inactiveFile.equals(other.inactiveFile))
			return false;
		if (kernelStack == null) {
			if (other.kernelStack != null)
				return false;
		} else if (!kernelStack.equals(other.kernelStack))
			return false;
		if (slab == null) {
			if (other.slab != null)
				return false;
		} else if (!slab.equals(other.slab))
			return false;
		if (sreclaim == null) {
			if (other.sreclaim != null)
				return false;
		} else if (!sreclaim.equals(other.sreclaim))
			return false;
		if (sunreclaim == null) {
			if (other.sunreclaim != null)
				return false;
		} else if (!sunreclaim.equals(other.sunreclaim))
			return false;
		if (swapCached == null) {
			if (other.swapCached != null)
				return false;
		} else if (!swapCached.equals(other.swapCached))
			return false;
		if (swapFree == null) {
			if (other.swapFree != null)
				return false;
		} else if (!swapFree.equals(other.swapFree))
			return false;
		if (swapTotal == null) {
			if (other.swapTotal != null)
				return false;
		} else if (!swapTotal.equals(other.swapTotal))
			return false;
		if (unevictable == null) {
			if (other.unevictable != null)
				return false;
		} else if (!unevictable.equals(other.unevictable))
			return false;
		if (vmallocChunk == null) {
			if (other.vmallocChunk != null)
				return false;
		} else if (!vmallocChunk.equals(other.vmallocChunk))
			return false;
		if (vmallocTotal == null) {
			if (other.vmallocTotal != null)
				return false;
		} else if (!vmallocTotal.equals(other.vmallocTotal))
			return false;
		if (vmallocUsed == null) {
			if (other.vmallocUsed != null)
				return false;
		} else if (!vmallocUsed.equals(other.vmallocUsed))
			return false;
		if (writeback == null) {
			if (other.writeback != null)
				return false;
		} else if (!writeback.equals(other.writeback))
			return false;
		return true;
	}
}
