package CPSF.com.demo.service.processor.task;

import CPSF.com.demo.model.EmailData;
import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.processor.TaskService;

import java.util.UUID;

import static CPSF.com.demo.model.constant.ReservationStatus.UNVERIFIED;
import static CPSF.com.demo.model.constant.TaskStatus.EXECUTED;
import static CPSF.com.demo.model.constant.TaskStatus.PENDING;
import static CPSF.com.demo.model.constant.TaskType.SEND_EMAIL_TASK;
import static CPSF.com.demo.model.constant.TaskType.WEB_APP_RESERVATION_TASK;

public class WebAppReservationTask implements ExecutableTask {

    private final ReservationService reservationService;
    private final TaskService taskService;

    private final Task emailTaskEntity = new Task();
    private final Task webAppReservationTask = new Task();

    public WebAppReservationTask(
            ReservationService reservationService,
            TaskService taskService,
            ReservationDTO payload
    ) {
        webAppReservationTask.setTaskType(WEB_APP_RESERVATION_TASK);
        webAppReservationTask.setPayload(payload);
        this.reservationService = reservationService;
        this.taskService = taskService;
    }

    @Override
    public Task getEntity() {
        return webAppReservationTask;
    }

    @Override
    public void doTask() {
        final var reservationDTO = (ReservationDTO) webAppReservationTask.getPayload();
        reservationService.create(
                reservationDTO.toBuilder().reservationStatus(UNVERIFIED).build());

        webAppReservationTask.setTaskStatus(EXECUTED);
        webAppReservationTask.setTargetId(UUID.randomUUID().toString());
        taskService.create(webAppReservationTask); //TODO maybe deleted later, I thought about having some history data

        prepareEmailTask(webAppReservationTask, reservationDTO);
    }

    private void prepareEmailTask(Task webAppReservationTask, ReservationDTO reservationDTO) {
        //load email
        emailTaskEntity.setPayload(new EmailData());
        emailTaskEntity.setTaskType(SEND_EMAIL_TASK);
        emailTaskEntity.setParentTask(webAppReservationTask);
        emailTaskEntity.setTaskStatus(PENDING);
        emailTaskEntity.setTargetId(UUID.randomUUID().toString());
        taskService.create(emailTaskEntity);
    }

}
