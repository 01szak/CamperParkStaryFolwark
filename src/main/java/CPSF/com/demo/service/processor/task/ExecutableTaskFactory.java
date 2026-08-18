package CPSF.com.demo.service.processor.task;

import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.processor.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ExecutableTaskFactory {

    private final ReservationService reservationService;
    private final TaskService taskService;

    public ExecutableTask getExecutableTask(Task task) {
        switch (task.getTaskType()) {
            case SEND_EMAIL_TASK -> {
                return getSendEmailTask(task);
            }
            case WEB_APP_RESERVATION_TASK -> {
                return getWebAppReservationTask(task);
            }
            default -> throw new UnsupportedOperationException("Unsupported task");
        }
    }

    private WebAppReservationTask getWebAppReservationTask(Task task) {
        return new WebAppReservationTask(
                reservationService,
                taskService,
                task
        );
    }

    private SendEmailTask getSendEmailTask(Task task) {
        return new SendEmailTask(task);
    }

}
