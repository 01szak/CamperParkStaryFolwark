package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.PaidReservationsDTO;
import CPSF.com.demo.DTO.ReservationMetadataDTO;
import CPSF.com.demo.DTO.Reservation_DTO;
import CPSF.com.demo.util.DtoMapper;
import CPSF.com.demo.util.ReservationMetadataMapper;
import CPSF.com.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationMetadataMapper reservationMetadataMapper;

    @Autowired
    private ReservationService reservationService;

//    @PostMapping
//    public ResponseEntity<?> create(@RequestBody Reservation_DTO reservationDto) {
//        reservationService.create(reservationDto.checkin(), reservationDto.checkout(), reservationDto.camperPlaceIndex(), reservationDto.guest());
//        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Rezeracja została dodana"));
//    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Reservation_DTO request) {
        reservationService.update(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Rezeracja została zmieniona"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        reservationService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Rezeracja została usunięta"));
    }

//        TODO to refactor
    @GetMapping
    public Page<Reservation_DTO> findAll(Pageable pageable,
                                        @RequestParam(required = false) String by,
                                        @RequestParam(required = false) String value)
            throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        if (by != null && value != null) {
            return reservationService.findBy(pageable, by, value).map(DtoMapper::getReservationDto);
        }
        return reservationService.findAll(pageable).map(DtoMapper::getReservationDto);
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





