package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.service.core.CamperPlaceTypeService;
import CPSF.com.demo.service.util.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/camperPlaceType")
public class CamperPlaceTypeController {

    @Autowired
    private CamperPlaceTypeService camperPlaceTypeService;

    @GetMapping
    public List<CamperPlaceTypeDTO> getTypes() {
        return camperPlaceTypeService.findAll().map(DtoMapper::getCamperPlaceTypeDTO).toList();
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> createOrUpdate(@RequestBody @Valid List<CamperPlaceTypeDTO> camperPlaceTypeDTOs) {
        camperPlaceTypeService.createOrUpdate(camperPlaceTypeDTOs);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Typy parcel zostały dodane / zmienione"));
    }
}
