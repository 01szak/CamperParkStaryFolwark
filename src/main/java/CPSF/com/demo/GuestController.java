package CPSF.com.demo;

import CPSF.com.demo.Entity.Guest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class GuestController {

    @PostMapping("/registration")
    public String registration(Model model){

        model.addAttribute("guest",new Guest());

        return "registration-form";
    }

}
