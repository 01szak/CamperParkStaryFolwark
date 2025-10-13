    package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.request.CamperPlaceRequest;
import CPSF.com.demo.service.CamperPlaceService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/camperPlaces")
public class CamperPlaceController {

    private final CamperPlaceService camperPlaceService;

    public CamperPlaceController(CamperPlaceService camperPlaceService) {
        this.camperPlaceService = camperPlaceService;
    }

    @GetMapping
    public List<CamperPlaceDTO> findAllCamperPlacesDTO() {
        return camperPlaceService.findAllDTO().toList();
    }

    @PostMapping("/create")
    public void createCamperPlace(@RequestBody CamperPlaceRequest request) {
        camperPlaceService.create(request.type(), BigDecimal.valueOf(request.price()));
    }

    @GetMapping("/getCamperPlaceTypes")
    public List<String> getTypes() {
        return Arrays.stream(Type.values()).map(Enum::toString).toList();
    }

    @DeleteMapping("/deleteCamperPlace/{index}")
    public void deleteCamperPlace(@PathVariable String index) {
        camperPlaceService.deleteByIndex(index);
    }
    }
