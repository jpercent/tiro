package syndeticlogic.tiro.controller;

import syndeticlogic.tiro.controller.ControllerMeta.MemoryType;

public interface IOController {
    Long getId();
    MemoryType getMemoryType();
    boolean notDone();
    IOExecutor getNextIOExecutor();
}
