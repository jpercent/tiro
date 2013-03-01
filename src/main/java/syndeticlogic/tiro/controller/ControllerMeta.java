package syndeticlogic.tiro.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ControllerMeta {
    public enum ControllerType {SequentialScan, SequentialWrite, RandomRead, RandomWrite, Random};
    public enum ExecutorType {SyncFileChannel, SyncCatena};
    public enum MemoryType {Java , Native, JavaToNativeToDisk};
    
    private long id;
    private final String controllerType;
    private final String executorType;
    private final String memoryType;
    private final String device;
    
    public ControllerMeta(String controllerType, String executor, String memoryType, String device) {
        this.controllerType = controllerType;
        this.executorType = executor;
        this.memoryType = memoryType;
        this.device = device;
        this.id = -1;
    }
    
    public ControllerMeta(long id, String name, String executor, String memoryType, String device) {
        this(name, executor, memoryType, device);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ControllerType getControllerType() {
        return ControllerType.valueOf(controllerType);
    }

    public String getControllerTypeName() {
        return controllerType;
    }

    public ExecutorType getExecutorType() {
        return ExecutorType.valueOf(executorType);
    }

    public String getExecutorTypeName() {
        return executorType;
    }
    
    public MemoryType getMemoryType() {
        return MemoryType.valueOf(memoryType);
    }
    
    public String getMemoryTypeName() {
        return memoryType;
    }

    public String getDevice() {
        return device;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((controllerType == null) ? 0 : controllerType.hashCode());
        result = prime * result + ((device == null) ? 0 : device.hashCode());
        result = prime * result + ((executorType == null) ? 0 : executorType.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((memoryType == null) ? 0 : memoryType.hashCode());
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
        ControllerMeta other = (ControllerMeta) obj;
        if (controllerType == null) {
            if (other.controllerType != null)
                return false;
        } else if (!controllerType.equals(other.controllerType))
            return false;
        if (device == null) {
            if (other.device != null)
                return false;
        } else if (!device.equals(other.device))
            return false;
        if (executorType == null) {
            if (other.executorType != null)
                return false;
        } else if (!executorType.equals(other.executorType))
            return false;
        if (id != other.id)
            return false;
        if (memoryType == null) {
            if (other.memoryType != null)
                return false;
        } else if (!memoryType.equals(other.memoryType))
            return false;
        return true;
    }
    
    public static class ControllerMetaRowMapper implements RowMapper<ControllerMeta> {
        @Override
        public ControllerMeta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ControllerMeta(rs.getLong("id"), rs.getString("name"), rs.getString("executor"), rs.getString("memory"),
                    rs.getString("device"));
        }
    } 
}
