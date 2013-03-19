package syndeticlogic.tiro.persistence;

import java.util.LinkedList;
import syndeticlogic.tiro.persistence.CpuStats;
import java.util.List;

public class LinuxCpuStats implements CpuStats {
    private LinkedList<Double> user;
    private LinkedList<Double> system;
    private LinkedList<Double> iowait;
    private LinkedList<Double> idle;
    
    public LinuxCpuStats() {
        user = new LinkedList<Double>();
        system = new LinkedList<Double>();
        iowait = new LinkedList<Double>();
        idle = new LinkedList<Double>();
    }    
    
    public double getAverageUserModeTime() {
        return  Stats.computeAverage(user);
    }
    
    public List<Double> getRawUserModeTime(){
        return user;
    }

    public double getAverageSystemModeTime()  {
        return  Stats.computeAverage(system);
    }
    
    public List<Double> getRawSystemModeTime(){
        return system;
    }
    
    public double getAverageIowaitTime()  {
        return  Stats.computeAverage(iowait);
    }
    
    public List<Double> getRawIowaitTime(){
        return iowait;
    }
    
    public double getAverageIdleModeTime()  {
        return  Stats.computeAverage(idle);
    }
    
    public List<Double> getRawIdleModeTime() {
        return idle;
    }

    public void addRawRecord(Double userTime, Double systemTime, Double iowaitTime, Double idleTime) {
        user.add(userTime);
        system.add(systemTime);
        iowait.add(iowaitTime);
        idle.add(idleTime);
    }    

    public void dumpData() {
        System.out.println("User Mode Time:   "+user);
        System.out.println("System Mode Time: "+system);
        System.out.println("IOWait Time:      "+iowait);
        System.out.println("Idle Time:        "+idle);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idle == null) ? 0 : idle.hashCode());
		result = prime * result + ((iowait == null) ? 0 : iowait.hashCode());
		result = prime * result + ((system == null) ? 0 : system.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LinuxCpuStats))
			return false;
		LinuxCpuStats other = (LinuxCpuStats) obj;
		if (idle == null) {
			if (other.idle != null)
				return false;
		} else if (!idle.equals(other.idle))
			return false;
		if (iowait == null) {
			if (other.iowait != null)
				return false;
		} else if (!iowait.equals(other.iowait))
			return false;
		if (system == null) {
			if (other.system != null)
				return false;
		} else if (!system.equals(other.system))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
