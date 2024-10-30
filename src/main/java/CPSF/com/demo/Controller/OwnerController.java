package CPSF.com.demo.Controller;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.Service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class OwnerController {

    private OwnerService ownerService;
    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }


    @GetMapping( "/guestsManagement")
    public List<Guest> getGuests(){

        List<Guest> guests = ownerService.findAllGuests();


        return guests;

    }

//    @DeleteMapping("/guestsManagement/{occupiedPlace}")
//    public String deleteGuestByOccupiedPlace(){
//
//        return "";
//    }
//    @GetMapping("/aboutGuest")
//    public String aboutGuest(){
//
//        return "aboutGuest-form";
//}
@GetMapping("/guestsManagement/{occupiedPlace}")
 public Guest findGuestByOccupiedPlace(@PathVariable int occupiedPlace){
    Guest guest = ownerService.findGuestByOccupiedPlace(occupiedPlace);

        return guest;
    }





}
