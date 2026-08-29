package CPSF.com.demo.service.processor.task;

import CPSF.com.demo.model.EmailData;
import CPSF.com.demo.model.constant.TaskStatus;
import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.processor.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static CPSF.com.demo.model.constant.ReservationStatus.UNVERIFIED;
import static CPSF.com.demo.model.constant.TaskType.SEND_EMAIL_TASK;

@Slf4j
@RequiredArgsConstructor
public class WebAppReservationTask implements ExecutableTask {

    private static final String NO_RESERVATION_PAYLOAD_MESSAGE = "There is no reservation to store";
    private static final String NO_RESERVATION_CREATOR_MESSAGE = "Reservation payload does not contains any creator";

    private final ReservationService reservationService;
    private final TaskService taskService;
    private final Task webAppReservationTaskEntity;

    @Override
    public Task getEntity() {
        return webAppReservationTaskEntity;
    }

    @Override
    public void doTask() {
        final var reservationDTO = (ReservationDTO) webAppReservationTaskEntity.getPayload();

        if (reservationDTO == null) {
            failTaskWithMessage(NO_RESERVATION_PAYLOAD_MESSAGE);
            return;
        }

        final var creator = reservationDTO.creator();

        if (creator == null) {
            failTaskWithMessage(NO_RESERVATION_CREATOR_MESSAGE);
            return;
        }

        final var modifiedReservation = reservationDTO.toBuilder()
                .reservationStatus(UNVERIFIED)
                .build();

        reservationService.create(modifiedReservation);

        prepareEmailTask(reservationDTO);
    }

    private void prepareEmailTask(ReservationDTO reservationDTO) {
        final var sharedTargetId = UUID.randomUUID().toString();
        //load email
        final var emailTaskEntity = Task.builder()
                .targetId(sharedTargetId)
                .payload(new EmailData()) //TODO fill this object
                .taskType(SEND_EMAIL_TASK)
                .build();

        prepareReservationVerifierTask(sharedTargetId);

        taskService.create(emailTaskEntity);
    }

    private void prepareReservationVerifierTask(String sharedTargetId) {
        //TODO create this child event
    }

    private void failTaskWithMessage(String message) {
        webAppReservationTaskEntity.setTaskStatus(TaskStatus.FAILED);
        webAppReservationTaskEntity.setStatusMessage(message);
    }

}

