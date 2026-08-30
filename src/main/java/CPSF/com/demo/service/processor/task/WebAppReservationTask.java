package CPSF.com.demo.service.processor.task;

import CPSF.com.demo.model.EmailData;
import CPSF.com.demo.model.constant.Operation;
import CPSF.com.demo.model.constant.TaskStatus;
import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.core.GuestService;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.core.SearchCriteria;
import CPSF.com.demo.service.processor.TaskService;
import CPSF.com.demo.service.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.UUID;

import static CPSF.com.demo.model.constant.ReservationStatus.UNVERIFIED;
import static CPSF.com.demo.model.constant.TaskType.SEND_EMAIL_AUTHENTICATION_TASK;

@Slf4j
@RequiredArgsConstructor
public class WebAppReservationTask implements ExecutableTask {
    //TODO if reservation is created and email fails, user may want to retry and he wouldn't be able since the reservation already exists, it has to be covered in business logic
    private static final String NO_RESERVATION_PAYLOAD_MESSAGE = "There is no reservation to store";
    private static final String NO_RESERVATION_CREATOR_MESSAGE = "Reservation payload does not contains any creator";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ReservationService reservationService;
    private final TaskService taskService;
    private final GuestService guestService;
    private final Task webAppReservationTaskEntity;

    @Override
    public Task getEntity() {
        return webAppReservationTaskEntity;
    }

    @Override
    public void doTask() {
        final var reservationDTO =
                objectMapper.convertValue(webAppReservationTaskEntity.getPayload(), ReservationDTO.class);

        if (reservationDTO == null) {
            failTaskWithMessage(NO_RESERVATION_PAYLOAD_MESSAGE);
            return;
        }

        final var creator = reservationDTO.creator();

        if (creator == null) {
            failTaskWithMessage(NO_RESERVATION_CREATOR_MESSAGE);
            return;
        }

        var guestOpt =
                guestService.findBy(new SearchCriteria("email", Operation.LIKE, reservationDTO.guest().email())).get().findFirst();

        //If the email already exists and guest already had made reservation in the past we just pass the existing guest with updated data
        final var guest = guestOpt.isPresent()
                ? DtoMapper.getGuestDTO(guestService.update(guestOpt.get()))
                : reservationDTO.guest();

        final var modifiedReservation = reservationDTO.toBuilder()
                .guest(guest)
                .reservationStatus(UNVERIFIED)
                .build();

        final var createdReservation = reservationService.create(modifiedReservation);

        //I need to pass guest entity cause I need guest id
        prepareEmailTask(createdReservation.getGuest());
    }

    private void prepareEmailTask(Guest guest) {
        final var sharedTargetId = UUID.randomUUID().toString();
        //load email
        final var emailTaskEntity = Task.builder()
                .targetId(sharedTargetId)
                .payload(new EmailData(guest))
                .taskType(SEND_EMAIL_AUTHENTICATION_TASK)
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

