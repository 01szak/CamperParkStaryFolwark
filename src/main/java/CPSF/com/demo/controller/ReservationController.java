package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.PaidReservationsDTO;
import CPSF.com.demo.DTO.ReservationDTO;
import CPSF.com.demo.DTO.ReservationMetadataDTO;
import CPSF.com.demo.repository.ReservationRepository;
import CPSF.com.demo.request.ReservationRequest;
import CPSF.com.demo.util.ReservationMetadataMapper;
import CPSF.com.demo.service.implementation.ReservationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Transactional
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationMetadataMapper reservationMetadataMapper;
    private final ReservationServiceImpl reservationService;
    private final ReservationRepository repository;

    @PostMapping("/createReservation")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request) {
        reservationService.create(request.checkin(), request.checkout(), request.camperPlaceIndex(), request.user());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Sukces","Rezeracja została dodana"));
    }

    @PatchMapping("updateReservation/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable int id, @RequestBody ReservationRequest request) {
        reservationService.update(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Sukces","Rezeracja została zmieniona"));
    }

    @DeleteMapping("deleteReservation/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable int id) {
        reservationService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("Sukces","Rezeracja została usunięta"));
    }

    @GetMapping("/")
    public Page<ReservationDTO> findAll(Pageable pageable,
                                        @RequestParam(required = false) String by,
                                        @RequestParam(required = false) String value)
            throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        if (by != null && value != null) {
            return reservationService.findDTOBy(pageable, by, value);
        }
        return reservationService.findAllDTO(pageable);
    }

    @GetMapping({"/getReservationMetadata"})
    public Map<String, ReservationMetadataDTO> getReservationMetadata() {
        return reservationService.getReservationMetadataDTO();
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
    public Map<String, Map<String,List<String>>> getUserPerReservation() {
        return reservationMetadataMapper.getUserPerReservation();
    }

}





