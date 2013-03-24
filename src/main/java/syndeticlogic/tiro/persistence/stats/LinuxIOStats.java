package syndeticlogic.tiro.persistence.stats;

import java.util.LinkedList;
import java.util.List;

public class LinuxIOStats implements IOStats {
    private final String device;
    private final LinkedList<Double> tps;
    private final LinkedList<Double> kpsRead;
    private final LinkedList<Double> kpsWritten;
    
    public LinuxIOStats(String device) {
        this.device = device;
        kpsRead = new LinkedList<Double>();
        tps = new LinkedList<Double>();
        kpsWritten = new LinkedList<Double>();
    }    
    
    public String getDevice() {
        return device;
    }
    
    public double getAverageTps() {
        return  Stats.computeAverage(tps);
    }
    
    public List<Double> getRawTps()  {
        return tps;
    }
    
    public double getAverageKpsRead() {
        return Stats.computeAverage(kpsRead);
    }
    
    public List<Double> getRawKpsRead() {
        return kpsRead;
    }

    public double getAverageKpsWritten() {
        return  Stats.computeAverage(kpsWritten);
    }
    
    public List<Double> getRawKpsWritten()  {
        return kpsWritten;
    }
    
    public void addRawRecord(Double tpsValue, Double kpsReadValue, Double kpsWrittenValue) {
        kpsRead.add(tpsValue);
        tps.add(kpsReadValue);
        kpsWritten.add(kpsWrittenValue);
    }    

    public void dumpData() {
        System.out.println("Transfers per Second["+device+"]: "+tps);
    	System.out.println("Kilobytes per Transfer["+device+"]: "+kpsRead);
        System.out.println("Megabytes per Transfer["+device+"]: "+kpsWritten);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((kpsRead == null) ? 0 : kpsRead.hashCode());
        result = prime * result + ((kpsWritten == null) ? 0 : kpsWritten.hashCode());
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
        LinuxIOStats other = (LinuxIOStats) obj;
        if (kpsRead == null) {
            if (other.kpsRead != null)
                return false;
        } else if (!kpsRead.equals(other.kpsRead))
            return false;
        if (kpsWritten == null) {
            if (other.kpsWritten != null)
                return false;
        } else if (!kpsWritten.equals(other.kpsWritten))
            return false;
        if (tps == null) {
            if (other.tps != null)
                return false;
        } else if (!tps.equals(other.tps))
            return false;
        return true;
    }
}
