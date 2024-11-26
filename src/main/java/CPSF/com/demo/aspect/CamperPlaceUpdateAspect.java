package CPSF.com.demo.aspect;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.service.ReservationService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@EnableAspectJAutoProxy
public class CamperPlaceUpdateAspect {
    private final CamperPlaceService camperPlaceService;
    private final ReservationService reservationService;

    public CamperPlaceUpdateAspect(CamperPlaceService camperPlaceService, ReservationService reservationService) {
        this.camperPlaceService = camperPlaceService;
        this.reservationService = reservationService;
    }
    @Before("execution(* CPSF.com.demo.controller.*.*(..))")
    public void setIsOccupiedIfReservationContinuesAtTheMoment(){
        List<Reservation> allReservations = reservationService.findAllReservations();
        for(Reservation reservation : allReservations){
            camperPlaceService.setIsOccupiedIfReservationContinuesAtTheMoment(reservation);
        }

    }
}
