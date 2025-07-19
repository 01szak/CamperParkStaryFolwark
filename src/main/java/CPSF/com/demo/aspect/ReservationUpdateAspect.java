package CPSF.com.demo.aspect;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.service.implementation.ReservationServiceImpl;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@EnableAspectJAutoProxy
@AllArgsConstructor
public class ReservationUpdateAspect {

    private final ReservationServiceImpl reservationService;

    @Before("execution(* CPSF.com.demo.controller.CamperPlaceController.finAllCamperPlaces(..)) || " +
            "execution(* CPSF.com.demo.controller.ReservationController.getFilteredReservations(..))")
    public void setReservationStatusAndIsCamperPlaceOccupied() {
        List<Reservation> reservations = reservationService.getAll();
        reservations.forEach(reservationService::updateReservationStatus);
    }


}
