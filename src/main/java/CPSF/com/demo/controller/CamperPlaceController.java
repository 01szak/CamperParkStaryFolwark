package CPSF.com.demo.controller;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.DTO.CamperPlaceDTO;
import CPSF.com.demo.entity.DTO.CamperPlaceRequest;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.service.CamperPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/camperPlace")
public class CamperPlaceController {
    private final CamperPlaceService camperPlaceService;

    @Autowired
    public CamperPlaceController(CamperPlaceService camperPlaceService) {
        this.camperPlaceService = camperPlaceService;
    }


    @PostMapping("/create")
    public void createCamperPlace(@RequestBody CamperPlaceRequest request) {

        camperPlaceService.createCamperPlace(request.type(), request.price());

    }

    @GetMapping("/getAll")
    public List<CamperPlaceDTO> finAllCamperPlaces() {
        return camperPlaceService.findAllCamperPlacesDTO();

    }

    @GetMapping("/getCamperPlaceTypes")
    public List<String> getTypes() {
        return Arrays.stream(Type.values()).map(Enum::toString).toList();
    }

    @DeleteMapping("/deleteCamperPlace/{index}")
    public void deleteCamperPlace(@PathVariable String index) {
        camperPlaceService.deleteCamperPlace(index);
    }

    @GetMapping("/find/{index}")
    public CamperPlace findCamperPlaceByCamperPlaceNumber(@PathVariable String index) {
        return camperPlaceService.findCamperPlaceByIndex(index);
    }
}
