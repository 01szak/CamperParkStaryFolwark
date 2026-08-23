package CPSF.com.demo.service.processor.task;

import CPSF.com.demo.model.entity.Task;

public interface ExecutableTask {
    Task getEntity();
    void doTask();
}
