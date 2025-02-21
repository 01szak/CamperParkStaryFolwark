package CPSF.com.demo.controller;

import CPSF.com.demo.entity.DTO.ReservationDto;
import CPSF.com.demo.entity.DTO.ReservationRequest;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.service.ReservationService;
import CPSF.com.demo.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    ReservationService reservationService;
    UserService userService;

    @Autowired
    public ReservationController(ReservationService theReservationService, @Lazy UserService userService) {
        this.reservationService = theReservationService;
        this.userService = userService;
    }

    @PostMapping("/createReservation")
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequest request) {

        reservationService.createReservation(request.checkin(), request.checkout(), request.camperPlace(), request.user());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/find/{reservationId}")
    public Reservation findReservationById(@PathVariable int reservationId) {
        Reservation reservation = reservationService.findReservationById(reservationId);
        return reservation;
    }


    @GetMapping({"/getFilteredReservations/{value}", "/getFilteredReservations"})
    public List<ReservationDto> getFilteredReservations(@PathVariable(required = false) String value) {
        if (value != null && value.trim().isEmpty()) {
            value = null;
        }
        return reservationService.getFilteredData(value);
    }

    @GetMapping("sortTable/{header}/{isAsc}")
    public List<ReservationDto> getSortedReservations(@PathVariable String header, @PathVariable int isAsc) {
        return reservationService.getSortedReservations(header, isAsc);
    }

    @PatchMapping("updateReservation/{id}")
    public void updateReservation(@PathVariable int id, @RequestBody ReservationRequest request) {
        reservationService.updateReservation(id, request);

    }


}





