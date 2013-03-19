package syndeticlogic.tiro.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OSXAggregatedIOStats implements AggregatedIOStats {
    private final Map<String, OSXIOStats> stats;
    private final List<Double> averageTps;
    private final List<Double> averageMbs;
    
    public OSXAggregatedIOStats(Map<String, OSXIOStats> stats) {
        this.stats = stats;
        averageTps = new ArrayList<Double>(stats.keySet().size());
        averageMbs = new ArrayList<Double>(stats.keySet().size());
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
