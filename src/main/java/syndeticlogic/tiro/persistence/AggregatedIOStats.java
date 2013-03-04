package syndeticlogic.tiro.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AggregatedIOStats {
    private final Map<String, IOStats> stats;
    private final List<Double> averageKbt;
    private final List<Double> averageTps;
    private final List<Double> averageMbs;
    
    public AggregatedIOStats(Map<String, IOStats> stats) {
        this.stats = stats;
        averageKbt = new ArrayList<Double>(stats.keySet().size());
        averageTps = new ArrayList<Double>(stats.keySet().size());
        averageMbs = new ArrayList<Double>(stats.keySet().size());
    }    

    public double getAverageKilobytesPerTransfer() {
        int index = 0;
        for(String device : stats.keySet()) {
            averageKbt.set(index++, Stats.computeAverage(stats.get(device).getRawMegabytesPerSecond()));
        }
        return Stats.computeAverage(averageKbt);
    }

    public double getAverageTransfersPerSecond() {
        int index = 0;
        for(String device : stats.keySet()) {
            averageTps.set(index++, Stats.computeAverage(stats.get(device).getRawMegabytesPerSecond()));
        }
        return Stats.computeAverage(averageTps);
    }

    public double getAverageMegabytesPerSecond() {
        int index = 0;
        for(String device : stats.keySet()) {
            averageMbs.set(index++, Stats.computeAverage(stats.get(device).getRawMegabytesPerSecond()));
        }
        return Stats.computeAverage(averageMbs);
    }

    public void addRawRecord(String device, Double kbtValue, Double tpsValue, Double mbsValue) {
        stats.get(device).addRawRecord(kbtValue, tpsValue, mbsValue);
    }    
}
