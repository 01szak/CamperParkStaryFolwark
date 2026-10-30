package CPSF.com.demo.service.processor.task.webappreservationflow;

import CPSF.com.demo.exception.AuthenticationException;
import CPSF.com.demo.model.constant.ReservationStatus;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.processor.TaskService;
import CPSF.com.demo.service.processor.task.ExecutableTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ReservationVerifierTask implements ExecutableTask {

    private final TaskService taskService;
    private final ReservationService reservationService;
    private final Task reservationStatusVerifierTaskEntity;

    @Override
    public void doTask() {
        final var holderTaskOpt = Optional.ofNullable(
                taskService.findPayloadHolderTaskByTargetId(reservationStatusVerifierTaskEntity.getTargetId()));

        holderTaskOpt.ifPresentOrElse(this::verifyReservation, () -> {throw new AuthenticationException("Reservation expired");});
    }

    private void verifyReservation(Task task) {
        final var reservationId = objectMapper.convertValue(task.getPayload(), Integer.class);
        final var reservationToVerify = reservationService.findById(reservationId);
        if (reservationToVerify.getReservationStatus() != ReservationStatus.UNVERIFIED) {
            log.error(
                    "Unexpected scenario: Other process already verified reservation with id: {}", reservationToVerify.getId());
        } else  {
            reservationToVerify.setReservationStatus(ReservationStatus.VERIFIED);
            reservationService.update(reservationToVerify);
        }
    }
}
