package syndeticlogic.tiro.stat;

import java.util.LinkedList;

public class OsxMemoryStats implements MemoryStats {
    private LinkedList<Long> freePages;
    private LinkedList<Long> activePages;
    private LinkedList<Long> speculative;
    private LinkedList<Long> inactivePages;
    private LinkedList<Long> wiredPages;
    private LinkedList<Long> faultRoutineCalls;
    private LinkedList<Long> copyOnWriteFaults;
    private LinkedList<Long> zeroFilledPages;
    private LinkedList<Long> reactivePages;
    private LinkedList<Long> pageIns;
    private LinkedList<Long> pageOuts;

    public OsxMemoryStats() {
        freePages = new LinkedList<Long>();
        activePages = new LinkedList<Long>();
        speculative = new LinkedList<Long>();
        inactivePages = new LinkedList<Long>();
        wiredPages = new LinkedList<Long>();    
        faultRoutineCalls = new LinkedList<Long>();
        copyOnWriteFaults = new LinkedList<Long>();
        zeroFilledPages = new LinkedList<Long>();
        reactivePages = new LinkedList<Long>();
        pageIns = new LinkedList<Long>();
        pageOuts = new LinkedList<Long>();
    }
    
    public double getAverageFreePages() {
        return Stats.computeAverage(freePages);
    }

    public LinkedList<Long> getRawFreePageMeasurements() {
        return freePages;
    }

    public double getAverageActivePages() {
        return Stats.computeAverage(activePages);
    }
    
    public LinkedList<Long> getRawActivePageMeasurements()  {
        return freePages;
    }

    public double getAverageSpeculative() {
        return Stats.computeAverage(speculative);
    }

    public LinkedList<Long> getRawSpeculatvieMeasurements()  {
        return speculative;
    }

    public double getAverageInactivePages() {
        return Stats.computeAverage(inactivePages);
    }

    public LinkedList<Long> getRawInactivePagesMeasurements()  {
        return inactivePages;
    }
    public double getAverageWiredPages() {
        return Stats.computeAverage(wiredPages);
    }
    
    public LinkedList<Long> getRawWiredPagesMeasurements()   {
        return wiredPages;
    }
    
    public double getAverageNumberOfFaultRoutineCalls() {
        return Stats.computeAverage(faultRoutineCalls);
    }
    
    public LinkedList<Long> getRawNumberOfFaultRoutineCallMeasurements()   {
        return faultRoutineCalls;
    }
    
    public double getAverageCopyOnWriteFaults() {
        return Stats.computeAverage(copyOnWriteFaults);
    }
    
    public LinkedList<Long> getRawCopyOnWriteFaultsMeasurements()   {
        return copyOnWriteFaults;
    }

    public double getAverageZeroFilledPages() {
        return Stats.computeAverage(zeroFilledPages);
    }
    
    public LinkedList<Long> getRawZeroFilledPageMeasurements()   {
        return zeroFilledPages;
    } 

    public double getAverageReactivatedPages() {
        return Stats.computeAverage(reactivePages);
    }
    
    public LinkedList<Long> getRawReactivatedPagesMeasurements()   {
        return reactivePages;
    }
    
    public double getAveragePageIns() {
        return Stats.computeAverage(pageIns);
    }
    
    public LinkedList<Long> getRawPageInMeasurements()   {
        return pageIns;
    }
    
    public double getAveragePageOuts() {
        return Stats.computeAverage(pageOuts);
    }
    
    public LinkedList<Long> getRawPageOutsMeasurements()   {
        return pageOuts;
    }

    public void addRawRecord(Long freePagesValue, Long activePagesValue, Long speculativeValue, Long inactivePagesValue, Long wiredPagesValue, 
            Long faultRoutineCallsValue, Long copyOnWriteFaultsValue, Long zeroFilledPagesValue, Long reactivePagesValue, Long pageInsValue, 
            Long pageOutsValue) 
    {
        freePages.add(freePagesValue);
        activePages.add(activePagesValue);
        speculative.add(speculativeValue);
        inactivePages.add(inactivePagesValue);
        wiredPages.add(wiredPagesValue);
        faultRoutineCalls.add(faultRoutineCallsValue);
        copyOnWriteFaults.add(copyOnWriteFaultsValue);
        zeroFilledPages.add(zeroFilledPagesValue);
        reactivePages.add(reactivePagesValue);
        pageIns.add(pageInsValue);
        pageOuts.add(pageOutsValue);
    }
    
    public void dumpData() {
        System.out.println("Free Pages:           "+freePages);
        System.out.println("Active Pages:         "+activePages);
        System.out.println("Speculative:          "+speculative);
        System.out.println("Inactive Pages:       "+inactivePages);
        System.out.println("Wired Pages:          "+wiredPages);
        System.out.println("Fault routine calls:  "+faultRoutineCalls);
        System.out.println("Copy on write faults: "+copyOnWriteFaults);
        System.out.println("Zero filled:          "+zeroFilledPages);
        System.out.println("Reactive pages:       "+reactivePages);
        System.out.println("Page ins:             "+pageIns);
        System.out.println("Page outs:            "+pageOuts);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activePages == null) ? 0 : activePages.hashCode());
        result = prime * result + ((copyOnWriteFaults == null) ? 0 : copyOnWriteFaults.hashCode());
        result = prime * result + ((faultRoutineCalls == null) ? 0 : faultRoutineCalls.hashCode());
        result = prime * result + ((freePages == null) ? 0 : freePages.hashCode());
        result = prime * result + ((inactivePages == null) ? 0 : inactivePages.hashCode());
        result = prime * result + ((pageIns == null) ? 0 : pageIns.hashCode());
        result = prime * result + ((pageOuts == null) ? 0 : pageOuts.hashCode());
        result = prime * result + ((reactivePages == null) ? 0 : reactivePages.hashCode());
        result = prime * result + ((speculative == null) ? 0 : speculative.hashCode());
        result = prime * result + ((wiredPages == null) ? 0 : wiredPages.hashCode());
        result = prime * result + ((zeroFilledPages == null) ? 0 : zeroFilledPages.hashCode());
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
        OsxMemoryStats other = (OsxMemoryStats) obj;
        if (activePages == null) {
            if (other.activePages != null)
                return false;
        } else if (!activePages.equals(other.activePages))
            return false;
        if (copyOnWriteFaults == null) {
            if (other.copyOnWriteFaults != null)
                return false;
        } else if (!copyOnWriteFaults.equals(other.copyOnWriteFaults))
            return false;
        if (faultRoutineCalls == null) {
            if (other.faultRoutineCalls != null)
                return false;
        } else if (!faultRoutineCalls.equals(other.faultRoutineCalls))
            return false;
        if (freePages == null) {
            if (other.freePages != null)
                return false;
        } else if (!freePages.equals(other.freePages))
            return false;
        if (inactivePages == null) {
            if (other.inactivePages != null)
                return false;
        } else if (!inactivePages.equals(other.inactivePages))
            return false;
        if (pageIns == null) {
            if (other.pageIns != null)
                return false;
        } else if (!pageIns.equals(other.pageIns))
            return false;
        if (pageOuts == null) {
            if (other.pageOuts != null)
                return false;
        } else if (!pageOuts.equals(other.pageOuts))
            return false;
        if (reactivePages == null) {
            if (other.reactivePages != null)
                return false;
        } else if (!reactivePages.equals(other.reactivePages))
            return false;
        if (speculative == null) {
            if (other.speculative != null)
                return false;
        } else if (!speculative.equals(other.speculative))
            return false;
        if (wiredPages == null) {
            if (other.wiredPages != null)
                return false;
        } else if (!wiredPages.equals(other.wiredPages))
            return false;
        if (zeroFilledPages == null) {
            if (other.zeroFilledPages != null)
                return false;
        } else if (!zeroFilledPages.equals(other.zeroFilledPages))
            return false;
        return true;
    }
    
}
