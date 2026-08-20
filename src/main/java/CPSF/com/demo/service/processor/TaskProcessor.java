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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.stream.Gatherer;

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

        final var taskForest = pendingTasks.stream().gather(toTaskForest()).toList();

        if (pendingTasks.isEmpty()) {
            return;
        }

        increaseRetryCount(pendingTasks);
        try(final var executor =  Executors.newVirtualThreadPerTaskExecutor()) {
            log.info("{} pending tasks found", pendingTasks.size());
            taskForest.forEach(node -> {
                executor.submit(() -> executeTaskNode(node));
            });
        } catch (Exception e) {
            log.error("Exception occurred while processing the tasks: {}", e.getLocalizedMessage());
            final var inProgressTasks =
                    (List<Task>) taskService.findBy(new SearchCriteria("taskStatus" , Operation.EQUALS, IN_PROGRESS.toString())).get().toList();
            taskService.update(inProgressTasks.stream().map(t -> mapTaskStatus(t, FAILED)).toList());
        }
    }

    private void executeTaskNode(TaskNode taskNode) {
        try {
            log.info("Starting to process task: {} ", taskNode.task().getTaskType());
            taskService.update(mapTaskStatus(taskNode.task(), IN_PROGRESS));
            executableTaskFactory.getExecutableTask(taskNode.task()).doTask();
            taskService.update(mapTaskStatus(taskNode.task(), EXECUTED));
            log.info("task: {} executed successfully", taskNode.task().getTaskType());
        } catch (Exception e) {
            log.error("Exception occurred while processing the task: {} {}", taskNode.task().getTaskType(), e);
            taskService.update(mapTaskStatus(taskNode.task(), FAILED));
            failDescendants(taskNode);
        }
    }

    private void failDescendants(TaskNode taskNode) {
        taskNode.subTasks().forEach(s -> {
            log.warn("Skipping child task {} with ID{} because parent task failed", s.task().getTaskType(), s.task.getId());
            taskService.update(mapTaskStatus(s.task(), FAILED));
            failDescendants(s);

        });
    }

    private void increaseRetryCount(List<Task> pendingTasks) {
        pendingTasks.stream()
                .filter(t -> FAILED.equals(t.getTaskStatus()))
                .forEach(t -> t.setRetryCount(t.getRetryCount() + 1));
    }

    private <T extends Task> T mapTaskStatus(T task, TaskStatus taskStatus) {
        task.setTaskStatus(taskStatus);
        return task ;
    }

    private record TaskNode(Task task, List<TaskNode> subTasks) {

        public TaskNode(Task task) {
            this(task, new ArrayList<>());
        }

        public void addSubTask(Task task) {
            subTasks().add(new TaskNode(task, new ArrayList<>()));
        }
    }

    private static Gatherer<Task, Map<Integer, TaskNode>, TaskNode> toTaskForest() {
        return Gatherer.ofSequential(
                HashMap::new,
                (map, task, downstream) -> {
                    map.put(task.getId(), new TaskNode(task));
                    return true;
                },
                (map, downstream) -> {
                    final var roots = new ArrayList<>();

                    map.values().forEach(v -> {
                        final var parent = v.task().getParentTask();

                        Optional.ofNullable(parent).ifPresentOrElse(t -> {
                            if (map.containsKey(parent.getId())) {
                                map.get(parent.getId()).addSubTask(v.task());
                            }
                        }, () -> {
                            roots.add(v.task());
                        });

                    });

                    roots.forEach(root -> {
                        if (!downstream.push((TaskNode) root)) return;
                    });
                }
        );
    }
}
