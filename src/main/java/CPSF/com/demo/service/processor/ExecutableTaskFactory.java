package CPSF.com.demo.service.processor;

import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.core.GuestService;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.processor.task.ExecutableTask;
import CPSF.com.demo.service.processor.task.webappreservationflow.ReservationVerifierTask;
import CPSF.com.demo.service.processor.task.webappreservationflow.SendEmailAuthenticationTask;
import CPSF.com.demo.service.processor.task.webappreservationflow.WebAppReservationTask;
import co.novu.Novu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ExecutableTaskFactory {

    private final ReservationService reservationService;
    private final TaskService taskService;
    private final Novu novu;
    private final GuestService guestService;

    public ExecutableTask getExecutableTask(Task task) {
        switch (task.getTaskType()) {
            case SEND_EMAIL_AUTHENTICATION_TASK -> {
                return getSendEmailTask(task);
            }
            case WEB_APP_RESERVATION_TASK -> {
                return getWebAppReservationTask(task);
            }
            case RESERVATION_STATUS_VERIFIER_TASK -> {
                return getReservationStatusVerifierTask(task);
            }
            default -> throw new UnsupportedOperationException("Unsupported task");
        }
    }

    private ReservationVerifierTask getReservationStatusVerifierTask(Task task) {
        return new ReservationVerifierTask(taskService, reservationService, task);
    }

    private WebAppReservationTask getWebAppReservationTask(Task task) {
        return new WebAppReservationTask(
                reservationService,
                taskService,
                guestService,
                task
        );
    }

    private SendEmailAuthenticationTask getSendEmailTask(Task task) {
        return new SendEmailAuthenticationTask(novu, task);
    }

}
