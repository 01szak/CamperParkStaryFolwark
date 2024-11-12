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
    public ReservationController(ReservationService theReservationService, CamperPlaceService camperPlaceService, UserService userService){
        this.reservationService = theReservationService;
        this.camperPlaceService = camperPlaceService;
        this.userService = userService;
    }

    @PostMapping("/setReservation/{camperPlaceNumber}/{enter}/{checkout}")
    public Reservation setReservation(@PathVariable int camperPlaceNumber,@PathVariable LocalDate enter,@PathVariable LocalDate checkout){
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
    @GetMapping("/findAll")
    public List<Reservation> findAllReservations(){
        List<Reservation> reservations = reservationService.findAllReservations();
        return reservations;
        }

    @GetMapping("/findByUserId/{userId}")
    public Reservation findReservationByUserId(@RequestParam int userId){
        Reservation reservation = reservationService.getReservationByUserId(userId);
        return reservation;
    }
    }

