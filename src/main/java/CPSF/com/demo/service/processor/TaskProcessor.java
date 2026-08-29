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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static CPSF.com.demo.model.constant.TaskStatus.EXECUTED;
import static CPSF.com.demo.model.constant.TaskStatus.FAILED;
import static CPSF.com.demo.model.constant.TaskStatus.IN_PROGRESS;
import static CPSF.com.demo.model.constant.TaskStatus.PENDING;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskProcessor {

    private static final long MAX_RETRY_COUNT = 5;

    private final TaskService taskService;
    private final ExecutableTaskFactory executableTaskFactory;

    @Scheduled(fixedDelay = 10000L)
    public void processTask() {
        final var currentDateTimeStr = LocalDateTime.now().toString();
        final var pendingTasks =
                (List<Task>) taskService.findBy(
                        new SearchCriteria("executionDate", Operation.LESS_THEN, currentDateTimeStr),
                        new SearchCriteria("executionDate", Operation.EQUALS, currentDateTimeStr, JoinOperator.OR),
                        new SearchCriteria("taskStatus", Operation.EQUALS, FAILED.toString(), JoinOperator.AND),
                        new SearchCriteria("retryable", Operation.EQUALS, "true", JoinOperator.AND),
                        new SearchCriteria("retryCount", Operation.LESS_THEN, String.valueOf(MAX_RETRY_COUNT), JoinOperator.AND),
                        new SearchCriteria("taskStatus", Operation.EQUALS, PENDING.toString(), JoinOperator.OR)
                        ).get().toList();

        if (pendingTasks.isEmpty()) {
            return;
        }

        increaseRetryCountAndSetStatusToPending(pendingTasks);

        final var taskForest = pendingTasks.stream()
                .gather(TaskGatherer.createTaskForest())
                .collect(Collectors.toCollection(ArrayList::new));
        final var failedTaskNodes = taskForest.stream()
                .filter(tn -> FAILED.equals(tn.task().getTaskStatus()))
                .flatMap(this::flattenNode)
                .collect(Collectors.toCollection(ArrayList::new));

        if (!failedTaskNodes.isEmpty()) {
            taskForest.removeAll(failedTaskNodes);
            taskService.update(failedTaskNodes.stream().map(TaskNode::task).toList());
        }

        try(final var executor =  Executors.newVirtualThreadPerTaskExecutor()) {
            log.info("{} pending tasks found", pendingTasks.size());
            taskForest.forEach(node -> {
                executor.submit(() -> executeTaskNode(node));
            });
        } catch (Exception e) {
            log.error("Exception occurred while processing the tasks: {}", e.getLocalizedMessage());
            final var inProgressTasks =
                    (List<Task>) taskService.findBy(
                            new SearchCriteria("taskStatus" , Operation.EQUALS, IN_PROGRESS.toString())
                            ).get().toList();
            taskService.update(inProgressTasks.stream().map(t -> mapTaskStatus(t, FAILED, e.getLocalizedMessage())).toList());
        }
    }

    private void executeTaskNode(TaskNode taskNode) {
        try {
            log.info("Starting to process task: {} ", taskNode.task().getTaskType());
            taskService.update(mapTaskStatus(taskNode.task(), IN_PROGRESS));

            executableTaskFactory.getExecutableTask(taskNode.task()).doTask();

            if (FAILED.equals(taskNode.task().getTaskStatus())) {
                //if task failed while being processed we just stop executing the rest of the tree
                taskService.update(taskNode.task());
                failDescendants(taskNode);
                return;
            }

            taskService.update(mapTaskStatus(taskNode.task(), EXECUTED));
            log.info("task: {} executed successfully", taskNode.task().getTaskType());

            if (taskNode.subTasks().isEmpty()) return;

            try(final var executor =  Executors.newVirtualThreadPerTaskExecutor()) {
                taskNode.subTasks().forEach(subTask -> {
                    executor.submit(() -> {
                        executeTaskNode(subTask);
                    });
                });

            }

        } catch (Exception e) {
            log.error("Exception occurred while processing the task: {} {}", taskNode.task().getTaskType(), e);
            taskService.update(mapTaskStatus(taskNode.task(), FAILED));
            failDescendants(taskNode);
        }
    }

    private void failDescendants(TaskNode taskNode) {
        taskNode.subTasks().forEach(s -> {
            log.warn("Skipping child task {} with ID {} because parent task failed", s.task().getTaskType(), s.task().getId());
            taskService.update(mapTaskStatus(s.task(), FAILED));
            failDescendants(s);

        });
    }

    private void increaseRetryCountAndSetStatusToPending(List<Task> pendingTasks) {
        pendingTasks.stream()
                .filter(t -> FAILED.equals(t.getTaskStatus()))
                .forEach(t -> t.setRetryCount(t.getRetryCount() + 1));

        pendingTasks.forEach(t -> {
            if (t.getRetryCount() >= MAX_RETRY_COUNT) {
                t.setTaskStatus(FAILED);
                t.setStatusMessage("Retry count exceeded the maximum of " + MAX_RETRY_COUNT);
                log.warn(
                        "Task {} with ID {} permanently failed - retry count {} exceeded the maximum of {}",
                        t.getTaskType(), t.getId(), t.getRetryCount(), MAX_RETRY_COUNT
                );
            } else {
                t.setTaskStatus(PENDING);
            }
        });
    }

    private <T extends Task> T mapTaskStatus(T task, TaskStatus taskStatus, String statusMessage) {
        task.setTaskStatus(taskStatus);
        task.setStatusMessage(statusMessage);
        return task ;
    }

    private <T extends Task> T mapTaskStatus(T task, TaskStatus taskStatus) {
        return mapTaskStatus(task, taskStatus, null);
    }

    private Stream<TaskNode> flattenNode(TaskNode node) {
        return Stream.concat(
                Stream.of(node),
                node.subTasks().stream().flatMap(this::flattenNode)
        );
    }
}
