package CPSF.com.demo.aspect;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class ReservationUpdateAspect {

    private final ReservationService reservationService;


    @Before("execution(* CPSF.com.demo.controller.*.*(..))")
    public void setReservationStatusAndIsCamperPlaceOccupied() {
        List<Reservation> reservations = reservationService.findAllReservations();
        reservations.forEach(reservationService::updateReservationStatus);
    }


}
