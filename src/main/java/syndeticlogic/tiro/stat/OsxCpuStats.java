package syndeticlogic.tiro.stat;

import java.util.LinkedList;

import syndeticlogic.tiro.stat.CpuStats;

import java.util.List;

public class OsxCpuStats implements CpuStats {
    private LinkedList<Long> user;
    private LinkedList<Long> system;
    private LinkedList<Long> idle;
    
    public OsxCpuStats() {
        user = new LinkedList<Long>();
        system = new LinkedList<Long>();
        idle = new LinkedList<Long>();
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

    public void addRawRecord(Long userTime, Long systemTime, Long idleTime) {
        user.add(userTime);
        system.add(systemTime);
        idle.add(idleTime);
    }    

    public void dumpData() {
        System.out.println("User Mode Time:   "+user);
        System.out.println("System Mode Time: "+system);
        System.out.println("Idle Time:        "+idle);
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idle == null) ? 0 : idle.hashCode());
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
        if (getClass() != obj.getClass())
            return false;
        OsxCpuStats other = (OsxCpuStats) obj;
        if (idle == null) {
            if (other.idle != null)
                return false;
        } else if (!idle.equals(other.idle))
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
