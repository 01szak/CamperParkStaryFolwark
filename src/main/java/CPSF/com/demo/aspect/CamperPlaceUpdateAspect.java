package CPSF.com.demo.aspect;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Aspect
@Component
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class CamperPlaceUpdateAspect {

    private final ReservationService reservationService;


//    @Before("execution(* CPSF.com.demo.controller.*.*(..))")
//    @Transactional
//    public void setIsOccupiedAndReservationStatusDependingOnReservationDay(){
//        List<Reservation> allReservations = reservationService.findAllReservations();
//        for(Reservation reservation : allReservations){
//       setIsOccupiedAndReservationStatusDependingOnReservationDay(reservation);
//        }

//    }
//    public void setIsOccupiedAndReservationStatusDependingOnReservationDay(Reservation reservation) {
//        CamperPlace camperPlace = reservation.getCamperPlace();
//        Stream<LocalDate> daysBetweenEnterAndCheckout = reservation.getCheckin().datesUntil(reservation.getCheckout());
//        Stream<LocalDate> daysBetweenEnterAndCheckout2 = reservation.getCheckin().datesUntil(reservation.getCheckout());
//
//        if (daysBetweenEnterAndCheckout.anyMatch(date -> date.equals(LocalDate.now()))) {
//            reservation.setReservationStatus(ReservationStatus.ACTIVE);
//            camperPlace.setIsOccupied(true);
//        } else if (daysBetweenEnterAndCheckout2.anyMatch(date -> date.isBefore(LocalDate.now()))) {
//            reservation.setReservationStatus(ReservationStatus.EXPIRED);
//            camperPlace.setIsOccupied(false);
//        } else {
//            reservation.setReservationStatus(ReservationStatus.COMING);
//            camperPlace.setIsOccupied(false);
//
//        }
//
//
//    }
}
