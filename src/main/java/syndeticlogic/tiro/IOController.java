package syndeticlogic.tiro;

import syndeticlogic.tiro.ControllerMeta.MemoryType;

public interface IOController {
    Long getId();
    MemoryType getMemoryType();
    boolean notDone();
    IOExecutor getNextIOExecutor();
}
