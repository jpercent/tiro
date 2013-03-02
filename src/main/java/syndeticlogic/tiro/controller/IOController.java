package syndeticlogic.tiro.controller;

import syndeticlogic.tiro.persistence.ControllerMeta.MemoryType;

public interface IOController {
    Long getId();
    MemoryType getMemoryType();
    boolean notDone();
    IOExecutor getNextIOExecutor();
}
