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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idle == null) ? 0 : idle.hashCode());
        result = prime * result + ((kbt == null) ? 0 : kbt.hashCode());
        result = prime * result + ((mbs == null) ? 0 : mbs.hashCode());
        result = prime * result + ((system == null) ? 0 : system.hashCode());
        result = prime * result + ((tps == null) ? 0 : tps.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        IOStats other = (IOStats) obj;
        if (idle == null) {
            if (other.idle != null)
                return false;
        } else if (!idle.equals(other.idle))
            return false;
        if (kbt == null) {
            if (other.kbt != null)
                return false;
        } else if (!kbt.equals(other.kbt))
            return false;
        if (mbs == null) {
            if (other.mbs != null)
                return false;
        } else if (!mbs.equals(other.mbs))
            return false;
        if (system == null) {
            if (other.system != null)
                return false;
        } else if (!system.equals(other.system))
            return false;
        if (tps == null) {
            if (other.tps != null)
                return false;
        } else if (!tps.equals(other.tps))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }
}
