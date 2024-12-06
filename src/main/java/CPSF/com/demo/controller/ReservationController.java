package CPSF.com.demo.controller;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.service.ReservationService;
import CPSF.com.demo.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    @PostMapping("/createReservation")
    public void createReservation(
            @RequestParam int camperPlaceNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enter,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout) {

            reservationService.createReservation(camperPlaceNumber,enter,checkout);
    }

    @GetMapping("/find/{reservationId}")
    public Reservation findReservationById(@PathVariable int reservationId) {
        Reservation reservation = reservationService.findReservationById(reservationId);
        return reservation;
    }
    @GetMapping("/findByReservationStatus")
    public List<Reservation> findReservationsDependingOnStatus(@RequestParam ReservationStatus... args) {
        List<Reservation> reservations = reservationService.findReservationByReservationStatus(args);
        return reservations;
    }

    @GetMapping("/findAll")
    public List<Reservation> findAllReservations() {
        List<Reservation> reservations = reservationService.findAllReservations();
        return reservations;
    }

    @PutMapping("/updateReservation")
    public void updateReservation(
            @RequestParam int reservationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newCheckin,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newCheckout) {
        reservationService.updateReservation(reservationId, newCheckin, newCheckout);
    }

    @GetMapping("/find/user/{userId}")
    public List<Reservation> findReservationByUserId(@PathVariable int userId) {
        List<Reservation> reservations = reservationService.findByUserId(userId);
        return reservations;
    }
}

