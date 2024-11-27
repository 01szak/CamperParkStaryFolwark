package CPSF.com.demo.aspect;

import CPSF.com.demo.service.ReservationService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
public class ReservationUpdateAspect {

    ReservationService reservationService;

    ReservationUpdateAspect(ReservationService theReservationService) {
        this.reservationService = theReservationService;
    }


}