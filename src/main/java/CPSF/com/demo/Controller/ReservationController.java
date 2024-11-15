package CPSF.com.demo.Controller;

import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Entity.User;
import CPSF.com.demo.Service.CamperPlaceService;
import CPSF.com.demo.Service.ReservationService;
import CPSF.com.demo.Service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final CamperPlaceService camperPlaceService;
    private final UserService userService;

    public ReservationController(ReservationService theReservationService, CamperPlaceService camperPlaceService, UserService userService) {
        this.reservationService = theReservationService;
        this.camperPlaceService = camperPlaceService;
        this.userService = userService;
    }

    @PostMapping("/setReservation")
    public Reservation setReservation(int camperPlaceNumber, LocalDate enter, LocalDate checkout) {
        Reservation reservation = new Reservation();

        if (camperPlaceService.isCamperPlaceOccupied(camperPlaceNumber).equals(false)) {

            CamperPlace camperPlace = camperPlaceService.findCamperPlaceById(camperPlaceNumber);
            reservationService.setReservation(reservation, camperPlace, enter, checkout);

            System.out.println(
                    "You have successfully made a reservation: \nid: "
                            + camperPlace.getId()
                            + "\ndate: " + enter + "/" + checkout);
        } else {

            System.out.println("The place you have chosen is occupied");

        }
        return reservation;


    }

    @GetMapping("/find/{reservationId}")
    public Reservation findReservationById(int reservationId) {
        Reservation reservation = reservationService.findReservationById(reservationId);
        return reservation;
    }

    @GetMapping("/find")
    public List<Reservation> findAllReservations() {
        List<Reservation> reservations = reservationService.findAllReservations();
        return reservations;
    }

    @PutMapping("/updateReservation/{reservationId}/{newCheckin}/{newCheckout}")
    public void updateReservation(@PathVariable int reservationId, @PathVariable LocalDate newCheckin, @PathVariable LocalDate newCheckout) {
        reservationService.updateReservation(reservationId, newCheckin, newCheckout);
    }

    @GetMapping("/find/{userId}")
    public Reservation findReservationByUserId(@PathVariable int userId) {
        Reservation reservation = reservationService.findByUser_id(userId);
        return reservation;
    }
}

