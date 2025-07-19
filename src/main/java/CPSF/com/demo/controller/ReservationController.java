package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.PaidReservationsDTO;
import CPSF.com.demo.DTO.ReservationDTO;
import CPSF.com.demo.DTO.ReservationMetadataDTO;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.request.ReservationRequest;
import CPSF.com.demo.util.ReservationMetadataMapper;
import CPSF.com.demo.service.implementation.ReservationServiceImpl;
import CPSF.com.demo.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Transactional
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationMetadataMapper reservationMetadataMapper;
    private final ReservationServiceImpl reservationService;
    private final UserServiceImpl userService;

    Reservation reservation;

    @Autowired
    public ReservationController(ReservationServiceImpl theReservationService, @Lazy UserServiceImpl userService, ReservationMetadataMapper reservationMetadataMapper) {
        this.reservationService = theReservationService;
        this.userService = userService;
        this.reservationMetadataMapper = reservationMetadataMapper;
    }

    @PostMapping("/createReservation")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        reservation =
                reservationService.createReservation(request.checkin(), request.checkout(), request.camperPlaceIndex(), request.user());

        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("updateReservation/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable int id, @RequestBody ReservationRequest request) {
        reservation = reservationService.updateReservation(id, request);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("deleteReservation/{id}")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable int id) {
        reservation = reservationService.deleteReservation(id);
        return ResponseEntity.ok(reservation);

    }

    @GetMapping("/find/{reservationId}")
    public Reservation findReservationById(@PathVariable int reservationId) {
        return reservationService.findReservationById(reservationId);
    }


    @GetMapping({"/getFilteredReservations/{value}", "/getFilteredReservations"})
    public List<ReservationDTO> getFilteredReservations(@PathVariable(required = false) String value) {
        if (value != null && value.trim().isEmpty()) {
            value = null;
        }
        return reservationService.getFilteredData(value);
    }

    @GetMapping({"/getReservationMetadata"})
    public Map<String, ReservationMetadataDTO> getReservationMetadata() {
        return reservationService.getReservationMetadataDTO();
    }

    @GetMapping("/getAll")
    public List<ReservationDTO> getAll() {
        return reservationService.getFilteredData("");
    }

    @GetMapping("sortTable/{header}/{isAsc}")
    public List<ReservationDTO> getSortedReservations(@PathVariable String header, @PathVariable int isAsc) {
        return reservationService.getSortedReservations(header, isAsc);
    }

    @GetMapping("/getPaidReservations")
    public Map<String, PaidReservationsDTO> getPaidReservations() {
        return reservationMetadataMapper.getPaidReservations();
    }

    @GetMapping("/getUnPaidReservations")
    public Map<String, PaidReservationsDTO> getUnPaidReservations() {
        return reservationMetadataMapper.getUnPaidReservations();
    }

    @GetMapping("/getUserPerReservation")
    public Map<String, Map<String,Set<String>>> getUserPerReservation() {
        return reservationMetadataMapper.getUserPerReservation();
    }

}





