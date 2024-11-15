package CPSF.com.demo.Controller;

import CPSF.com.demo.Enum.Type;
import CPSF.com.demo.Service.CamperPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void createCamperPlace(Type type, Double price){
        camperPlaceService.createCamperPlace(type,price);

    }
}
