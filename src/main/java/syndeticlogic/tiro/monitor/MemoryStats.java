package syndeticlogic.tiro.monitor;

import java.util.LinkedList;

public class MemoryStats {
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

    public MemoryStats() {
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
    
}
