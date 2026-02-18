    package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.CamperPlace_DTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.service.core.CamperPlaceService;
import CPSF.com.demo.service.core.CamperPlaceTypeService;
import CPSF.com.demo.service.util.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/camperPlace")
public class CamperPlaceController {

    @Autowired
    private CamperPlaceService camperPlaceService;

    @Autowired
    private CamperPlaceTypeService camperPlaceTypeService;

    @GetMapping
    public List<CamperPlace_DTO> getCamperPlaces() {
        return camperPlaceService.findAllOrderByIndex().stream().map(DtoMapper::getCamperPlaceDto).toList();
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> update(@RequestBody @Valid List<CamperPlace_DTO> camperPlaceDto) {
        if (camperPlaceDto.stream().anyMatch(c -> c.type().id() == null)) {
            throw new IllegalArgumentException("Invalid type");
        }

        var cp = new ArrayList<CamperPlace>();

        camperPlaceDto.forEach(dto -> {
            var c = camperPlaceService.findById(dto.id());
            c.setCamperPlaceType(camperPlaceTypeService.findById(dto.type().id()));
            c.setIndex(dto.index());
            c.setPrice(dto.price());
            cp.add(c);
        });

        camperPlaceService.update(cp);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success","Parcele zostały zmienione"));
    }

}
