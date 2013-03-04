package syndeticlogic.tiro.persistence;

import java.util.LinkedList;
import java.util.List;

public class IOStats {
    private final String device;
    private final LinkedList<Double> kbt;
    private final LinkedList<Double> tps;
    private final LinkedList<Double> mbs;
    
    public IOStats(String device) {
        this.device = device;
        kbt = new LinkedList<Double>();
        tps = new LinkedList<Double>();
        mbs = new LinkedList<Double>();
    }    
    
    public String getDevice() {
        return device;
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
    
    public void addRawRecord(Double kbtValue, Double tpsValue, Double mbsValue) {
        kbt.add(kbtValue);
        tps.add(tpsValue);
        mbs.add(mbsValue);
    }    

    public void dumpData() {
        System.out.println("Kilobytes per Transfer["+device+"]: "+kbt);
        System.out.println("Transfers per Second["+device+"]: "+tps);
        System.out.println("Megabytes per Transfer["+device+"]: "+mbs);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((kbt == null) ? 0 : kbt.hashCode());
        result = prime * result + ((mbs == null) ? 0 : mbs.hashCode());
        result = prime * result + ((tps == null) ? 0 : tps.hashCode());
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
        if (tps == null) {
            if (other.tps != null)
                return false;
        } else if (!tps.equals(other.tps))
            return false;
        return true;
    }
}
