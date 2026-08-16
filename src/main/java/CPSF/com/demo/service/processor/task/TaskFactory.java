package CPSF.com.demo.service.processor.task;

import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.processor.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class TaskFactory {

    private final ReservationService reservationService;
    private final TaskService taskService;

    public WebAppReservationTask getWebAppReservationTask(ReservationDTO payload) {
        return new WebAppReservationTask(
                reservationService,
                taskService,
                payload
        );
    }

    public SendEmailTask getSendEmailTask() {
        return new SendEmailTask();
    }

}
