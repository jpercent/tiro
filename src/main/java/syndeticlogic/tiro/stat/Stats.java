package syndeticlogic.tiro.stat;

import java.util.List;

public class Stats {
	public static double computeAverage(List<?> values) {
		assert values != null;
		
		if (values.size() == 0)
			return 0.0;
		
		if(values.get(0) instanceof Long) {
			long sum = 0;
			for(Object value : values) {
				sum += (Long)value;
			}
			return (double)sum/(double)values.size();
		} else if(values.get(0) instanceof Double) {
			double sum = 0;
			for(Object value : values) {
				sum += (Double)value;
			}
			return sum/(double)values.size();
		} else {
			throw new RuntimeException("unsupported type");
		}
	}
}