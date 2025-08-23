package CPSF.com.demo.validator;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static CPSF.com.demo.enums.ReservationStatus.ACTIVE;
import static CPSF.com.demo.enums.ReservationStatus.EXPIRED;

@Service
public class ReservationStatusValidator implements StatusValidator {

    private final ReservationRepository repository;

    public ReservationStatusValidator(ReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Scheduled(cron = "0 0 12 * * *")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void validateStatus() {
        List<Reservation> unexpiredReservations =
                repository.findByReservationStatusNot(EXPIRED);
        List<Reservation> updatedReservations = new ArrayList<>();

        for (Reservation r : unexpiredReservations) {
            Optional<Reservation> temp = returnReservationWithCorrectStatus(r);
            temp.ifPresent(updatedReservations::add);
        }

        repository.saveAll(updatedReservations);
    }

    private Optional<Reservation> returnReservationWithCorrectStatus(Reservation reservation) {
        LocalDate checkin = reservation.getCheckin();
        LocalDate checkout = reservation.getCheckout();
        LocalDate currentDate = LocalDate.now();
        ReservationStatus previousStatus =
                reservation.getReservationStatus();

        if (checkout.isBefore(currentDate)) {
            reservation.setReservationStatus(EXPIRED);
        } else if (isActive(checkin, checkout, currentDate)) {
            reservation.setReservationStatus(ACTIVE);
        }

        if (!previousStatus.equals(reservation.getReservationStatus())) {
            return Optional.of(reservation);
        }

        return Optional.empty();
    }

    private static boolean isActive(
            LocalDate checkin, LocalDate checkout, LocalDate currentDate) {
        return !currentDate.isBefore(checkin) && !currentDate.isAfter(checkout);
    }
}
