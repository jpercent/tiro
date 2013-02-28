package syndeticlogic.tiro;

public interface IOExecutor {
    public enum ExecutorType { Sync, Async };
    public IORecord getIORecord();
    public void setIORecord(IORecord ioRecord);
    boolean  performIO() throws Exception;
}
