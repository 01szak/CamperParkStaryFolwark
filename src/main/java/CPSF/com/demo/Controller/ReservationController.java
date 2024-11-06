package CPSF.com.demo.Controller;

import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Service.ReservationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    @PersistenceContext
    private EntityManager entityManager;
    public ReservationController(ReservationService theReservationService){
        this.reservationService = theReservationService;
    }

    @PostMapping("/setReservation")
    public Reservation setReservation(Model model, int camperPlaceNumber, LocalDate dateEnter, LocalDate checkout){
        Reservation reservation = new Reservation();

        TypedQuery<CamperPlace> camperPlace = entityManager.createQuery(

                "SELECT c FROM CamperPlace c WHERE c.id = :camperPlaceNumber",CamperPlace.class
        );
        camperPlace.setParameter("camperPlaceNumber",camperPlaceNumber);
        CamperPlace theCamperPlace = camperPlace.getSingleResult();

        dateEnter.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        checkout.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        if(theCamperPlace.getIsOccupied() == 0){
            reservation.setCamperPlace(theCamperPlace);
            reservation.setDateEnter(dateEnter);
            reservation.setDateCheckout(checkout);
        }else {
            System.out.println("The place you have chosen is occupied, choose another");
        }

        model.addAttribute("reservation",reservation);

        return reservation;

    }
}
