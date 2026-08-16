    package CPSF.com.demo.service.processor;


    import CPSF.com.demo.model.constant.Operation;
    import CPSF.com.demo.model.constant.TaskStatus;
    import CPSF.com.demo.model.entity.Task;
    import CPSF.com.demo.service.core.SearchCriteria;
    import CPSF.com.demo.service.processor.task.ExecutableTask;
    import CPSF.com.demo.service.processor.task.TaskFactory;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.log4j.Log4j2;
    import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
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
        private final TaskFactory taskFactory;

        @Scheduled(fixedDelay = 10000L)
        public void processTask() {
            //TODO add flow for failed task retry
            final var pendingTasks =
                    (List<Task>) taskService.findBy(new SearchCriteria("taskStatus" , Operation.EQUALS, PENDING.toString())).get().toList();

            if (pendingTasks.isEmpty()) {
                return;
            }

            try(final var executor =  Executors.newVirtualThreadPerTaskExecutor()) {
                log.info(pendingTasks.size() + " pending tasks found");
                pendingTasks.forEach(t -> {
                    executor.submit(()-> {
                        try {
                            taskService.update(mapTaskStatus(t, IN_PROGRESS));
                            getExecutableTask(t).doTask();
                            taskService.update(mapTaskStatus(t, EXECUTED));
                        } catch (Exception e) {
                            log.error("Exception occurred while processing the task: "
                                    + t.getTaskType() + e);
                            taskService.update(mapTaskStatus(t, FAILED));
                        }
                    });
                });
            } catch (Exception e) {
                log.error("Exception occurred while processing the tasks: " + e.getLocalizedMessage());
                final var inProgressTasks =
                        (ArrayList<Task>) taskService.findBy(new SearchCriteria("taskStatus" , Operation.EQUALS, IN_PROGRESS.toString())).get().toList();
                taskService.update(inProgressTasks.stream().map(t -> mapTaskStatus(t, FAILED)).toList());
            }
        }

        private ExecutableTask getExecutableTask(Task task) {
            switch (task.getTaskType()) {
                case SEND_EMAIL_TASK -> {
                    return taskFactory.getSendEmailTask();
                }
                case WEB_APP_RESERVATION_TASK -> {
                    //this task won't be processed by the processor, it is added just to keep track of all tasks
                    return taskFactory.getWebAppReservationTask(null);
                }
                default -> throw new UnsupportedOperationException("Unsupported task");
            }
        }


        private <T extends Task> T mapTaskStatus(T task, TaskStatus taskStatus) {
            task.setTaskStatus(taskStatus);
            return task ;
        }
    }
