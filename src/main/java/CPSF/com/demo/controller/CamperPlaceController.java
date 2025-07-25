package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.CamperPlaceDTO;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.request.CamperPlaceRequest;
import CPSF.com.demo.service.implementation.CamperPlaceServiceImpl;
import CPSF.com.demo.util.Mapper;
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
        camperPlaceService.create(request.type(), request.price());
    }

    @GetMapping("/getAll")
    public List<CamperPlaceDTO> finAllCamperPlaces() {
        return camperPlaceService.findAll().map(Mapper::toCamperPlaceDTO).toList();
    }

    @GetMapping("/getCamperPlaceTypes")
    public List<String> getTypes() {
        return Arrays.stream(Type.values()).map(Enum::toString).toList();
    }

    @DeleteMapping("/deleteCamperPlace/{index}")
    public void deleteCamperPlace(@PathVariable int index) {
        camperPlaceService.delete(index);
    }

    @GetMapping("/find/{id}")
    public CamperPlace findCamperPlaceByCamperPlaceNumber(@PathVariable int id) {
        return camperPlaceService.findById(id);
    }
}
