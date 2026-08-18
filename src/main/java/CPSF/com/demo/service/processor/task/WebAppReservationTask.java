package CPSF.com.demo.service.processor.task;

import CPSF.com.demo.model.EmailData;
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
        reservationService.create(
                reservationDTO.toBuilder().reservationStatus(UNVERIFIED).build());
        prepareEmailTask(webAppReservationTaskEntity, reservationDTO);
    }

    private void prepareEmailTask(Task webAppReservationTask, ReservationDTO reservationDTO) {
        //load email
        final var emailTaskEntity = Task.builder()
                .targetId(UUID.randomUUID().toString())
                .payload(new EmailData())
                .taskType(SEND_EMAIL_TASK)
                .parentTask(webAppReservationTask)
                .build();
        taskService.create(emailTaskEntity);
    }

}
