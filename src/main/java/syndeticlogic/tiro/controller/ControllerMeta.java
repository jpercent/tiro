package syndeticlogic.tiro.controller;

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
}
