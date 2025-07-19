package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.request.CamperPlaceRequest;
import CPSF.com.demo.service.implementation.CamperPlaceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/camperPlace")
public class CamperPlaceController {

    private final CamperPlaceServiceImpl camperPlaceService;

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
