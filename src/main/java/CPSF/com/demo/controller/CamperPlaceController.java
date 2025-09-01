package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.request.CamperPlaceRequest;
import CPSF.com.demo.service.CamperPlaceService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/camperPlace")
public class CamperPlaceController {

    private final CamperPlaceService camperPlaceService;

    public CamperPlaceController(CamperPlaceService camperPlaceService) {
        this.camperPlaceService = camperPlaceService;
    }

    @PostMapping("/create")
    public void createCamperPlace(@RequestBody CamperPlaceRequest request) {
        camperPlaceService.create(request.type(), request.price());
    }

    @GetMapping("/getAll")
    public List<CamperPlaceDTO> findAllCamperPlacesDTO() {
        return camperPlaceService.findAllDTO().toList();
    }

    @GetMapping("/getCamperPlaceTypes")
    public List<String> getTypes() {
        return Arrays.stream(Type.values()).map(Enum::toString).toList();
    }

    @DeleteMapping("/deleteCamperPlace/{index}")
    public void deleteCamperPlace(@PathVariable String index) {
        camperPlaceService.deleteByIndex(index);
    }

    @GetMapping("/find/{id}")
    public CamperPlace findCamperPlaceByCamperPlaceNumber(@PathVariable int id) {
        return camperPlaceService.findById(id);
    }
}
