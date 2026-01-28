    package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.DTO.CamperPlace_DTO;
import CPSF.com.demo.enums.CamperPlaceType;
import CPSF.com.demo.request.CamperPlaceRequest;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.util.Mapper;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/camperPlace")
public class CamperPlaceController {

    private final CamperPlaceService camperPlaceService;

    public record CamperPlaceTypeDTO(String name){}

    public CamperPlaceController(CamperPlaceService camperPlaceService) {
        this.camperPlaceService = camperPlaceService;
    }

    @GetMapping
    public List<CamperPlaceDTO> findAllCamperPlacesDTO() {
        return camperPlaceService.findAllDTO().toList();
    }

    @GetMapping("v2")
    public List<CamperPlace_DTO> getCamperPlaces() {
        return camperPlaceService.findAll().map(Mapper::toCamperPlace_DTO).stream().toList();
    }

    @PostMapping("/create")
    public void createCamperPlace(@RequestBody CamperPlaceRequest request) {
        camperPlaceService.create(request.camperPlaceType(), BigDecimal.valueOf(request.price()));
    }

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
