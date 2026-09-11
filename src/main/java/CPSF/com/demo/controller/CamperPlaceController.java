    package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.camperPlaceDTO;
import CPSF.com.demo.service.core.CamperPlaceService;
import CPSF.com.demo.service.util.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/camperPlace")
@RequiredArgsConstructor
public class CamperPlaceController {

    private final CamperPlaceService camperPlaceService;

    @GetMapping
    public List<camperPlaceDTO> getCamperPlaces() {
        return camperPlaceService.findAllOrderByIndex().stream().map(DtoMapper::getCamperPlaceDto).toList();
    }

    @GetMapping("{typeId}")
    public List<camperPlaceDTO> getCamperPlacesWithUniquePriceAndCamperTypeId(@PathVariable Integer typeId) {
        return camperPlaceService.findCamperPlaceByPriceNotNullAndCamperPlaceType_Id(typeId).stream()
                .map(DtoMapper::getCamperPlaceDto)
                .toList();
    }

    @GetMapping("/occupancy/{cpId}")
    public List<LocalDate> getOccupiedDate(@PathVariable Integer cpId) {
        return camperPlaceService.getOccupiedDates(cpId);
    }

    @GetMapping("/calcPrice/{cpId}/{checkin}/{checkout}")
    public BigDecimal getCalculatedReservationPrice(
            @PathVariable Integer cpId,
            @PathVariable LocalDate checkin,
            @PathVariable LocalDate checkout
    ) {
        return camperPlaceService.getCalculatedReservationPrice(cpId, checkin, checkout);
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> update(@RequestBody @Valid List<camperPlaceDTO> camperPlaceDtos) {
        camperPlaceService.updateAll(camperPlaceDtos);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Parcele zostały zmienione"));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody @Valid camperPlaceDTO camperPlaceDto) {
        camperPlaceService.create(camperPlaceDto);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Parcela została dodana"));
    }

    @DeleteMapping("/{campPlaceId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer campPlaceId) {
        camperPlaceService.deleteById(campPlaceId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Parcela została usunięta"));
    }

}
