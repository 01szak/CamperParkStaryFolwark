package CPSF.com.demo.aspect;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Aspect
@Component
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class CamperPlaceUpdateAspect {

    private final ReservationService reservationService;


    @Before("execution(* CPSF.com.demo.controller.*.*(..))")
    @Transactional
    public void setReservationStatusAndIsCamperPlaceOccupied(){
        List<Reservation> allReservations = reservationService.findAllReservations();
        allReservations.forEach(this::setReservationStatusAndIsCamperPlaceOccupied);
    }
    @Transactional
    public void setReservationStatusAndIsCamperPlaceOccupied(Reservation reservation) {
        CamperPlace camperPlace = reservation.getCamperPlace();
        List<LocalDate> reservedDays = reservation.getCheckin().datesUntil(reservation.getCheckout()).toList();

        if (reservedDays.contains(LocalDate.now())) {
            reservation.setReservationStatus(ReservationStatus.ACTIVE);
            camperPlace.setIsOccupied(true);
        } else if (reservedDays.get(reservedDays.size() - 1).isBefore(LocalDate.now())) {
            reservation.setReservationStatus(ReservationStatus.EXPIRED);
            camperPlace.setIsOccupied(false);
        } else {
            reservation.setReservationStatus(ReservationStatus.COMING);
            camperPlace.setIsOccupied(false);

        }


    }
}
