package CPSF.com.demo.aspect;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.service.ReservationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@EnableAspectJAutoProxy
@AllArgsConstructor
public class ReservationUpdateAspect {

    ReservationService reservationService;

    @Before("execution(* CPSF.com.demo.controller.CamperPlaceController.finAllCamperPlaces(..)) || " +
            "execution(* CPSF.com.demo.controller.ReservationController.getFilteredReservations(..))")
    public void setReservationStatusAndIsCamperPlaceOccupied() {
        List<Reservation> reservations = reservationService.getAll();
        reservations.forEach(reservationService::updateReservationStatus);
    }


}
