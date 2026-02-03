    package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.DTO.CamperPlace_DTO;
import CPSF.com.demo.enums.CamperPlaceType;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.util.Mapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/camperPlace")
public class CamperPlaceController {

    private final CamperPlaceService camperPlaceService;

    public record CamperPlaceTypeDTO(String name){}

    public CamperPlaceController(CamperPlaceService camperPlaceService) {
        this.camperPlaceService = camperPlaceService;
    }

    @GetMapping
    public List<CamperPlace_DTO> findAllCamperPlacesDTO() {
        return camperPlaceService.findAllDTO().toList();
    }

    @GetMapping("v2")
    public List<CamperPlace_DTO> getCamperPlaces() {
        return camperPlaceService.findAll().map(Mapper::toCamperPlace_DTO).stream().toList();
    }

    @PatchMapping
    public ResponseEntity<Map<String,String>> update(@RequestBody @Valid CamperPlace_DTO[] camperPlaceDto) {
        var camperPlaces = Arrays.stream(camperPlaceDto).map(Mapper::toCamperPlace).toList();
        camperPlaceService.update(camperPlaces);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Parcele zostały zmienione"));
    }
//
//    @PostMapping("/create")
//    public void createCamperPlace(@RequestBody @Valid CamperPlace_DTO camperPlaceDto) {
//        camperPlaceService.create(camperPlaceDto.type(), camperPlaceDto.price());
//    }

    @GetMapping("/type")
    public List<CamperPlaceTypeDTO> getTypes() {
        return Arrays.stream(CamperPlaceType.values()).map(t ->
                new CamperPlaceTypeDTO(t.toString())).toList();
    }

    @DeleteMapping("/deleteCamperPlace/{index}")
    public void deleteCamperPlace(@PathVariable String index) {
        camperPlaceService.deleteByIndex(index);
    }
}
