package CPSF.com.demo.Controller;

import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Service.CamperPlaceService;
import CPSF.com.demo.Service.ReservationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Transactional
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final CamperPlaceService camperPlaceService;
    @PersistenceContext
    private EntityManager entityManager;

    public ReservationController(ReservationService theReservationService, CamperPlaceService camperPlaceService){
        this.reservationService = theReservationService;
        this.camperPlaceService = camperPlaceService;
    }

    @PostMapping("/setReservation")
    public Reservation setReservation(int camperPlaceNumber, LocalDate enter, LocalDate checkout){
        Reservation reservation = new Reservation();

        if (camperPlaceService.isCamperPlaceOccupied(camperPlaceNumber).equals(false)){

            CamperPlace camperPlace = camperPlaceService.findCamperPlaceById(camperPlaceNumber);
            reservationService.setReservation(reservation,camperPlace,enter,checkout);

            System.out.println(
                    "You have successfully made a reservation: \nid: "
                    + camperPlace.getId()
                    + "\ndate: " + enter + "/" + checkout);
        }else {

            System.out.println("The place you have chosen is occupied");

        }
        return reservation;
    }
}
