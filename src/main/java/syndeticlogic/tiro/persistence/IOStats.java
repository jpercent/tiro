package syndeticlogic.tiro.persistence;

import java.util.LinkedList;
import java.util.List;

public class IOStats {
    private LinkedList<Double> kbt;
    private LinkedList<Double> tps;
    private LinkedList<Double> mbs;
    
    private LinkedList<Long> user;
    private LinkedList<Long> system;
    private LinkedList<Long> idle;
    
    public IOStats() {
        kbt = new LinkedList<Double>();
        tps = new LinkedList<Double>();
        mbs = new LinkedList<Double>();
        user = new LinkedList<Long>();
        system = new LinkedList<Long>();
        idle = new LinkedList<Long>();
    }    
    
    public double getAverageKilobytesPerTransfer() {
        return Stats.computeAverage(kbt);
    }
    
    public List<Double> getRawKiloBytesPerTranferMeasurements() {
        return kbt;
    }
    
    public double getAverageTransfersPerSecond() {
        return  Stats.computeAverage(tps);
    }
    
    public List<Double> getRawTransfersPerSecond()  {
        return tps;
    }
    
    public double getAverageMegabytesPerSecond() {
        return  Stats.computeAverage(mbs);
    }
    
    public List<Double> getRawMegabytesPerSecond()  {
        return mbs;
    }
    
    public double getAverageUserModeTime() {
        return  Stats.computeAverage(user);
    }
    
    public List<Long> getRawUserModeTime(){
        return user;
    }
    
    public double getAverageSystemModeTime()  {
        return  Stats.computeAverage(system);
    }
    
    public List<Long> getRawSystemModeTime(){
        return system;
    }
    
    public double getAverageIdleModeTime()  {
        return  Stats.computeAverage(idle);
    }
    
    public List<Long> getRawIdleModeTime() {
        return idle;
    }

    public void addRawRecord(Double kbtValue, Double tpsValue, Double mbsValue, Long userTime, Long systemTime, Long idleTime) {
        kbt.add(kbtValue);
        tps.add(tpsValue);
        mbs.add(mbsValue);
        user.add(userTime);
        system.add(systemTime);
        idle.add(idleTime);
    }    

    public void dumpData() {
        System.out.println("Kilobytes per Transfer: "+kbt);
        System.out.println("Transfers per Second:   "+tps);
        System.out.println("Megabytes per Transfer: "+mbs);
        System.out.println("User Mode Time:         "+user);
        System.out.println("System Mode Time:       "+system);
        System.out.println("Idle Time:              "+idle);
    }
}
