package CPSF.com.demo.service.processor.task.webappreservationflow;

import CPSF.com.demo.exception.DateValidationException;
import CPSF.com.demo.model.EmailData;
import CPSF.com.demo.model.constant.Operation;
import CPSF.com.demo.model.constant.TaskStatus;
import CPSF.com.demo.model.constant.TaskType;
import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.core.GuestService;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.core.SearchCriteria;
import CPSF.com.demo.service.processor.TaskService;
import CPSF.com.demo.service.processor.task.ExecutableTask;
import CPSF.com.demo.service.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static CPSF.com.demo.model.constant.ReservationStatus.UNVERIFIED;
import static CPSF.com.demo.model.constant.TaskType.SEND_EMAIL_AUTHENTICATION_TASK;

@Slf4j
@RequiredArgsConstructor
public class WebAppReservationTask implements ExecutableTask {

    private static final String NO_RESERVATION_PAYLOAD_MESSAGE = "There is no reservation to store";
    private static final String NO_RESERVATION_CREATOR_MESSAGE = "Reservation payload does not contains any creator";

    private final ReservationService reservationService;
    private final TaskService taskService;
    private final GuestService guestService;
    private final Task webAppReservationTaskEntity;

    private record CreateReservationResult(boolean isRetry, Reservation createdReservation){};

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

        final var byEmail = SearchCriteria.builder().key("email").operation(Operation.EQUALS).value(reservationDTO.guest().email()).build();
        final var guestOpt = guestService.findBy(byEmail).stream().findFirst();
        //If the guest already exists (matched by email) we reuse that entity so the reservation
        //is linked to a persisted guest id.
        final var guest = guestOpt.isPresent() ? DtoMapper.getGuestDTO(guestService.update(guestOpt.get())) : reservationDTO.guest();
        final var modifiedReservation = reservationDTO.toBuilder().guest(guest).reservationStatus(UNVERIFIED).build();

        CreateReservationResult createReservationResult;
        try {
            createReservationResult = new CreateReservationResult(false, reservationService.create(modifiedReservation));
        } catch (DateValidationException e) {
            createReservationResult = handleRetry(guest, reservationDTO);
            if (!createReservationResult.isRetry()) {
                throw e;
            }
        }
        final var createdReservation = createReservationResult.createdReservation();
        if (!createReservationResult.isRetry()) {
            createReservationHolderTask(createdReservation.getId());
        }
        //we need to map the entity again cause we need to pass Id as well
        createEmailTask(DtoMapper.getGuestDTO(createdReservation.getGuest()), DtoMapper.getReservationDto(createdReservation));
    }

    private CreateReservationResult handleRetry(GuestDTO guest, ReservationDTO reservationDTO) {
        //we check whether the guest is retrying the reservation flow
        Reservation createdReservation = null;
        var isRetry = false;

        if (guest.id() == null) {
            return new CreateReservationResult(isRetry, createdReservation);
        }

        final var optCreatedReservation = reservationService.findBy(
                SearchCriteria.builder()
                        .joinObject("guest")
                        .key("id")
                        .operation(Operation.EQUALS)
                        .value(guest.id().toString())
                        .and()
                        .key("reservationStatus")
                        .operation(Operation.EQUALS)
                        .value(UNVERIFIED.toString())
                        .and()
                        .key("checkin")
                        .operation(Operation.EQUALS)
                        .value(reservationDTO.checkin().toString())
                        .and()
                        .key("checkout")
                        .operation(Operation.EQUALS)
                        .value(reservationDTO.checkout().toString())
                        .build()
        )
        .getContent();


        if (optCreatedReservation.size() == 1) {
            createdReservation = optCreatedReservation.getFirst();
            isRetry = true;
        }

        return new CreateReservationResult(isRetry, createdReservation);
    }

    private void createReservationHolderTask(Integer createdReservationId) {
        final var optHolderTask =
                Optional.ofNullable(taskService.findPayloadHolderTaskByTargetId(webAppReservationTaskEntity.getTargetId()));
        optHolderTask.or(() -> {
            final var holderTask = Task.builder()
                    .targetId(webAppReservationTaskEntity.getTargetId())
                    .payload(createdReservationId)
                    .taskStatus(TaskStatus.ON_HOLD)
                    .taskType(TaskType.TEMPORARY_PAYLOAD_HOLDER_TASK)
                    .build();

            taskService.create(holderTask);
            return Optional.empty();
        });
    }

    private void createEmailTask(GuestDTO guest, ReservationDTO reservationDTO) {
        final var emailTaskEntity = Task.builder()
                .targetId(webAppReservationTaskEntity.getTargetId())
                .payload(new EmailData(guest, reservationDTO))
                .taskType(SEND_EMAIL_AUTHENTICATION_TASK)
                .build();

        taskService.create(emailTaskEntity);
    }

    private void failTaskWithMessage(String message) {
        webAppReservationTaskEntity.setTaskStatus(TaskStatus.FAILED);
        webAppReservationTaskEntity.setStatusMessage(message);
    }

}

