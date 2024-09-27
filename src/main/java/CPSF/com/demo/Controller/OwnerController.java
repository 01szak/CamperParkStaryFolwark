package CPSF.com.demo.Controller;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.Service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class OwnerController {

    private OwnerService ownerService;
    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/guestsManagement")
    public String findAllGuests(Model model){
        List<Guest> guests = ownerService.findAllGuests();
        model.addAttribute("guests",guests);
        return "guestsManagement-form";
    }





}
