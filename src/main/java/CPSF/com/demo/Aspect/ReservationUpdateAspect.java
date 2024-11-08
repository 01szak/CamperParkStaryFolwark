package CPSF.com.demo.Aspect;

import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Service.ReservationService;
import jdk.jfr.Category;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Aspect
@Component
@EnableAspectJAutoProxy
public class ReservationUpdateAspect {

    ReservationService reservationService;

    ReservationUpdateAspect(ReservationService theReservationService) {
        this.reservationService = theReservationService;
    }

    @Before("execution(* CPSF.com.demo.Controller.*.*(..))")
    public void checkIfReservationExpired() {
        reservationService.deleteIfExpired();

    }

}