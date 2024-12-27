package CPSF.com.demo.controller;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.enums.Type;
import CPSF.com.demo.service.CamperPlaceService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    @Transactional
    public void createCamperPlace(
        @RequestBody Type type,
        @RequestBody @Positive(message = "Price can't be negative") BigDecimal price){

        camperPlaceService.createCamperPlace(type,price);

    }
    @GetMapping("/findAll")
    public List<CamperPlace> finAllCamperPlaces(){
        return camperPlaceService.findAllCamperPlaces();

    }
    @GetMapping("/getCamperPlaceTypes")
    public List<Type> getTypes(){
        return Arrays.stream(Type.values()).toList();
    }
}
