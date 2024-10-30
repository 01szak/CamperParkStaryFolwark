package CPSF.com.demo.Controller;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.Service.GuestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller

@RequestMapping("/guests")
public class GuestController {

    private GuestService guestService;



    public GuestController(GuestService theGuestService){

        guestService = theGuestService;

    }


    @GetMapping("/registration")
    public String registration(Model model){

        Guest guest = new Guest();

        model.addAttribute("guest",guest);

        return "registration-form";
    }

    @PostMapping("/save")
    public String saveGuest(@ModelAttribute("guest") Guest guest){



        guestService.save(guest);

        return "setOccupiedPlace-form";

    }
    @PostMapping("/confirm")
    public String confirmGuest(){

        return "confirmation-form";

    }
}
