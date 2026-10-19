    package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.SearchRequest;
import CPSF.com.demo.model.dto.CamperPlaceDTO;
import CPSF.com.demo.service.core.CamperPlaceService;
import CPSF.com.demo.service.util.DtoMapper;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/findBy")
    public Page<CamperPlaceDTO> findBy(@Nullable Pageable pageable, @RequestBody @Valid SearchRequest searchRequest) {
        return camperPlaceService.findBy(
                pageable,
                searchRequest.searchCriteria()
        ).map(DtoMapper::getCamperPlaceDto);
    }

    @GetMapping("{typeId}")
    public List<CamperPlaceDTO> getCamperPlacesWithUniquePriceAndCamperTypeId(@PathVariable Integer typeId) {
        return camperPlaceService.findCamperPlaceByPriceNotNullAndCamperPlaceType_Id(typeId).stream()
                .map(DtoMapper::getCamperPlaceDto)
                .toList();
    }

    @GetMapping("/occupancy/{cpId}")
    public List<List<LocalDate>> getOccupiedDates(@PathVariable Integer cpId) {
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

    @GetMapping("/available/{from}/{to}")
    public List<CamperPlaceDTO> getAvailableCamperPlaces(@PathVariable LocalDate from, @PathVariable LocalDate to) {
        return camperPlaceService.getAvailableCamperPlaces(from, to).stream().map(DtoMapper::getCamperPlaceDto).toList();
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> update(@RequestBody @Valid List<CamperPlaceDTO> camperPlaceDtos) {
        camperPlaceService.updateAll(camperPlaceDtos);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Parcele zostały zmienione"));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody @Valid CamperPlaceDTO camperPlaceDto) {
        camperPlaceService.create(camperPlaceDto);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Parcela została dodana"));
    }

    @DeleteMapping("/{campPlaceId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer campPlaceId) {
        camperPlaceService.deleteById(campPlaceId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Parcela została usunięta"));
    }

}
