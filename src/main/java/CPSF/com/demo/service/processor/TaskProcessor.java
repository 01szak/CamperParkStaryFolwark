package CPSF.com.demo.service.processor;


import CPSF.com.demo.model.constant.JoinOperator;
import CPSF.com.demo.model.constant.Operation;
import CPSF.com.demo.model.constant.TaskStatus;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.core.SearchCriteria;
import CPSF.com.demo.service.processor.task.ExecutableTaskFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;

import static CPSF.com.demo.model.constant.TaskStatus.EXECUTED;
import static CPSF.com.demo.model.constant.TaskStatus.FAILED;
import static CPSF.com.demo.model.constant.TaskStatus.IN_PROGRESS;
import static CPSF.com.demo.model.constant.TaskStatus.PENDING;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskProcessor {

    private final TaskService taskService;
    private final ExecutableTaskFactory executableTaskFactory;

    @Scheduled(fixedDelay = 10000L)
    public void processTask() {
        final var pendingTasks =
                (List<Task>) taskService.findBy(
                        new SearchCriteria("taskStatus", Operation.EQUALS, PENDING.toString()),
                        new SearchCriteria("taskStatus", Operation.EQUALS, FAILED.toString(), JoinOperator.OR),
                        new SearchCriteria("retryable", Operation.EQUALS, "true", JoinOperator.AND)
                        ).get().toList();

        if (pendingTasks.isEmpty()) {
            return;
        }

        increaseRetryCount(pendingTasks);
        try(final var executor =  Executors.newVirtualThreadPerTaskExecutor()) {
            log.info("{} pending tasks found", pendingTasks.size());
            pendingTasks.forEach(t -> {
                executor.submit(()-> {
                    try {
                        log.info("Starting to process task: {} ", t.getTaskType());
                        taskService.update(mapTaskStatus(t, IN_PROGRESS));
                        executableTaskFactory.getExecutableTask(t).doTask();
                        taskService.update(mapTaskStatus(t, EXECUTED));
                        log.info("task: {} executed successfully", t.getTaskType());
                    } catch (Exception e) {
                        log.error("Exception occurred while processing the task: {} {}", t.getTaskType(), e);
                        taskService.update(mapTaskStatus(t, FAILED));
                    }
                });
            });
        } catch (Exception e) {
            log.error("Exception occurred while processing the tasks: {}", e.getLocalizedMessage());
            final var inProgressTasks =
                    (List<Task>) taskService.findBy(new SearchCriteria("taskStatus" , Operation.EQUALS, IN_PROGRESS.toString())).get().toList();
            taskService.update(inProgressTasks.stream().map(t -> mapTaskStatus(t, FAILED)).toList());
        }
    }

    private static void increaseRetryCount(List<Task> pendingTasks) {
        pendingTasks.stream().filter(t -> FAILED.equals(t.getTaskStatus())).peek(t -> t.setRetryCount(t.getRetryCount() + 1));
    }

    private <T extends Task> T mapTaskStatus(T task, TaskStatus taskStatus) {
        task.setTaskStatus(taskStatus);
        return task ;
    }
}
