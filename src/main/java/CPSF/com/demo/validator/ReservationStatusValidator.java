package CPSF.com.demo.validator;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import CPSF.com.demo.util.ReservationCalculator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Scheduled(fixedRate = 1000L)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkIsPriceSet() {
        List<Reservation> reservations =
                repository.findAll();
        List<Reservation> updatedReservations = new ArrayList<>();

        for (Reservation r : reservations) {
            if (r.getPrice().compareTo(BigDecimal.ZERO) == 0) {
                r.setPrice(new ReservationCalculator().calculateFinalReservationCost(r.getCheckin(), r.getCheckout(), r.getCamperPlace().getPrice()));
                updatedReservations.add(r);
            }
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

        if (!Objects.equals(previousStatus, reservation.getReservationStatus())) {
            return Optional.of(reservation);
        }

        return Optional.empty();
    }

    private static boolean isActive(
            LocalDate checkin, LocalDate checkout, LocalDate currentDate) {
        return !currentDate.isBefore(checkin) && !currentDate.isAfter(checkout);
    }
}
