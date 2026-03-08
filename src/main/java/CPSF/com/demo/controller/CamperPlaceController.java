    package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.CamperPlace_DTO;
import CPSF.com.demo.service.core.CamperPlaceService;
import CPSF.com.demo.service.util.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/camperPlace")
@RequiredArgsConstructor
public class CamperPlaceController {

    private final CamperPlaceService camperPlaceService;

    @GetMapping
    public List<CamperPlace_DTO> getCamperPlaces() {
        return camperPlaceService.findAllOrderByIndex().stream().map(DtoMapper::getCamperPlaceDto).toList();
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> update(@RequestBody @Valid List<CamperPlace_DTO> camperPlaceDtos) {
        camperPlaceService.updateCamperPlaces(camperPlaceDtos);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Parcele zostały zmienione"));
    }

}
