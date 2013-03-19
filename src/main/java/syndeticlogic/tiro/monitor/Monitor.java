package syndeticlogic.tiro.monitor;

import java.util.List;

public interface Monitor {
	void start();
	void finish();
	void dumpData();
	long getStart();
	void recordStart();
	long getFinish();
	void recordFinish();
	long getDurationMillis();
	List<String> getCommandAndArgs();
	void setCommandAndArgs(String...comandAndArgs);
}