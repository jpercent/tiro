package syndeticlogic.tiro.persistence.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LinuxAggregatedIOStats implements AggregatedIOStats {
    private final Map<String, LinuxIOStats> stats;
    private final List<Double> averageTps;
    private final List<Double> averageKpsRead;
    private final List<Double> averageKpsWritten;
    
    public LinuxAggregatedIOStats(Map<String, LinuxIOStats> stats) {
        this.stats = stats;
        averageTps = new ArrayList<Double>(stats.keySet().size());
        averageKpsRead = new ArrayList<Double>(stats.keySet().size());
        averageKpsWritten = new ArrayList<Double>(stats.keySet().size());
    }

    public double getAverageTps() {
        int index = 0;
        for(String device : stats.keySet()) {
            averageTps.set(index++, Stats.computeAverage(stats.get(device).getRawTps()));
        }
        return Stats.computeAverage(averageTps);
    }

    public double getAverageKpsRead() {
        int index = 0;
        for(String device : stats.keySet()) {
            averageKpsRead.set(index++, Stats.computeAverage(stats.get(device).getRawKpsRead()));
        }
        return Stats.computeAverage(averageKpsRead);
    }

    public double getAverageKpsWritten() {
        int index = 0;
        for(String device : stats.keySet()) {
            averageKpsWritten.set(index++, Stats.computeAverage(stats.get(device).getRawKpsWritten()));
        }
        return Stats.computeAverage(averageKpsRead);
    }
    
    public void addRawRecord(String device, Double kbtValue, Double tpsValue, Double mbsValue) {
        stats.get(device).addRawRecord(kbtValue, tpsValue, mbsValue);
    }
}
