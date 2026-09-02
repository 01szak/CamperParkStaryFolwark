package CPSF.com.demo.service.processor.task;

import tools.jackson.databind.ObjectMapper;

public interface ExecutableTask {
    ObjectMapper objectMapper = new ObjectMapper();
    void doTask();
}
