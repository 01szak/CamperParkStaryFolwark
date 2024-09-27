package CPSF.com.demo.Controller;

import CPSF.com.demo.Service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class OwnerController {

    private OwnerService ownerService;
    @GetMapping("/guestsManagement")
    public String findAllGuests(){

        return "guestsManagement-form";
    }





}
