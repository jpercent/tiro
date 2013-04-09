package syndeticlogic.tiro.controller;

import syndeticlogic.tiro.model.ControllerMeta.MemoryType;

public interface IOController {
    Long getId();
    MemoryType getMemoryType();
    boolean notDone();
    IOExecutor getNextIOExecutor();
}
