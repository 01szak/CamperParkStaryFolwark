package CPSF.com.demo.controller;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.service.ReservationService;
import CPSF.com.demo.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public Reservation setReservation(
            @RequestParam int camperPlaceNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enter,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout) {

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
    public Reservation findReservationById(@PathVariable int reservationId) {
        Reservation reservation = reservationService.findReservationById(reservationId);
        return reservation;
    }

    @GetMapping("/find")
    public List<Reservation> findAllReservations() {
        List<Reservation> reservations = reservationService.findAllReservations();
        return reservations;
    }

    @PutMapping("/updateReservation")
    public void updateReservation(
            @RequestParam int reservationId,
            @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate newCheckin,
            @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newCheckout) {
        reservationService.updateReservation(reservationId, newCheckin, newCheckout);
    }

    @GetMapping("/find/user/{userId}")
    public List<Reservation> findReservationByUserId(@PathVariable int userId) {
        List<Reservation> reservations = reservationService.findByUser_id(userId);
        return reservations;
    }
}

