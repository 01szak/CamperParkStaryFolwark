package CPSF.com.demo.controller;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.DTO.ReservationDto;
import CPSF.com.demo.entity.DTO.ReservationRequest;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.enums.Role;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.service.ReservationService;
import CPSF.com.demo.service.UserService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Lazy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;


    public ReservationController(ReservationService theReservationService, @Lazy UserService userService) {
        this.reservationService = theReservationService;
        this.userService = userService;
    }

    @PostMapping("/createReservation")
    public void createReservation(@RequestBody ReservationRequest request) {
        reservationService.createReservation(request.checkin(), request.checkout(), request.camperPlace(), request.user());
    }


    @GetMapping("/find/{reservationId}")
    public Reservation findReservationById(@PathVariable int reservationId) {
        Reservation reservation = reservationService.findReservationById(reservationId);
        return reservation;
    }


    @GetMapping({"/getFilteredReservations/{value}", "/getFilteredReservations"})
    public List<ReservationDto> getFilteredReservations(@PathVariable(required = false)  String value) {
        if(value != null && value.trim().isEmpty()) {
            value = null;
        }
        return reservationService.getFilteredData(value);
    }

    @GetMapping("sortTable/{header}/{isAsc}")
    public List<ReservationDto> getSortedReservations(@PathVariable String header,@PathVariable int isAsc){
        return reservationService.getSortedReservations(header,isAsc);
    }
    @PatchMapping("updateReservation/{id}")
    public void updateReservation(@PathVariable int id,@RequestBody ReservationRequest request){
        reservationService.updateReservation(id,request);

    }



}





