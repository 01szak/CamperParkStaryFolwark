package CPSF.com.demo.controller;

import CPSF.com.demo.entity.DTO.ReservationDTO;
import CPSF.com.demo.entity.DTO.ReservationMetadataDTO;
import CPSF.com.demo.entity.DTO.ReservationRequest;
import CPSF.com.demo.entity.PaidReservations;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.service.ReservationMetadataService;
import CPSF.com.demo.service.ReservationService;
import CPSF.com.demo.service.UserService;
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

    private final ReservationMetadataService reservationMetadataService;
    private final ReservationService reservationService;
    private final UserService userService;

    Reservation reservation;

    @Autowired
    public ReservationController(ReservationService theReservationService, @Lazy UserService userService, ReservationMetadataService reservationMetadataService) {
        this.reservationService = theReservationService;
        this.userService = userService;
        this.reservationMetadataService = reservationMetadataService;
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
    public Map<String, PaidReservations> getPaidReservations() {
        return reservationMetadataService.getPaidReservations();
    }

    @GetMapping("/getUnPaidReservations")
    public Map<String, PaidReservations> getUnPaidReservations() {
        return reservationMetadataService.getUnPaidReservations();
    }

    @GetMapping("/getUserPerReservation")
    public Map<String, Map<String,Set<String>>> getUserPerReservation() {
        return reservationMetadataService.getUserPerReservation();
    }

}





