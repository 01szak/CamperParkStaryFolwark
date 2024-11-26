package CPSF.com.demo.controller;

import CPSF.com.demo.enums.Type;
import CPSF.com.demo.service.CamperPlaceService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Transactional
@RestController
@RequestMapping("/camperPlace")
public class CamperPlaceController {
private final CamperPlaceService camperPlaceService;
    @Autowired
    public CamperPlaceController(CamperPlaceService camperPlaceService) {
        this.camperPlaceService = camperPlaceService;
    }

    @PostMapping("/create")
    public void createCamperPlace(
        @RequestParam Type type,
        @RequestParam @Positive(message = "Price can't be negative") BigDecimal price){
        camperPlaceService.createCamperPlace(type,price);

    }
}
