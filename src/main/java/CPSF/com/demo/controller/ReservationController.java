package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.Reservation_DTO;
import CPSF.com.demo.service.core.SearchCriteria;
import CPSF.com.demo.service.util.DtoMapper;
import CPSF.com.demo.service.core.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody @Valid Reservation_DTO reservationDto) {
        reservationService.create(reservationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Rezeracja została dodana"));
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> update(@RequestBody @Valid Reservation_DTO reservationDto) {
        reservationService.update(reservationDto);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Rezeracja została zmieniona"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        reservationService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Rezeracja została usunięta"));
    }

    @GetMapping
    public Page<Reservation_DTO> findBy(Pageable pageable, SearchCriteria searchCriteria) {
        return reservationService.findBy(pageable, searchCriteria).map(DtoMapper::getReservationDto);
    }

}
