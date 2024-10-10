package CPSF.com.demo.Controller;

import CPSF.com.demo.Entity.Guest;
import CPSF.com.demo.OccupiedPlace;
import CPSF.com.demo.Service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class OwnerController {

    private OwnerService ownerService;
    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

//    @GetMapping("/guestsManagement")
//    public String findAllGuests(Model model){
//        List<Guest> guests = ownerService.findAllGuests();
//        model.addAttribute("guests",guests);
//        return "guestsManagement-form";
//    }

//    @GetMapping(value = "guestsManagement/", headers = "desc")
//    public String findGuestByOccupiedPlaceDesc(Model model){
//        List<Guest> guests = ownerService.findAllByOccupiedPlaceDesc();
//        model.addAttribute("guests",guests);
//
//
//        return "guestsManagement-form";
//
//    }
//
//    @GetMapping(value = "guestsManagement/", headers = "asc")
//    public String findGuestByOccupiedPlaceAsc(Model model){
//        List<Guest> guests = ownerService.findAllByOccupiedPlaceAsc();
//        model.addAttribute("guests",guests);
//
//
//        return "guestsManagement-form";
//
//    }
    @GetMapping(value = "/guestsManagement")
    public String sortTable(@RequestParam(defaultValue = "asc") String sortOrder, Model model){

        Sort.Direction direction = sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        List<Guest> guests = ownerService.getSortedTable(direction);

        model.addAttribute("guests",guests);
        model.addAttribute("sortOrderDirection",direction.toString());

        return "guestsManagement-form";

    }

    @DeleteMapping("/guestsManagement/{occupiedPlace}")
    public String deleteGuestByOccupiedPlace(){

        return "";
    }
    @GetMapping("/aboutGuest")
    public String aboutGuest(){

        return "aboutGuest-form";
}





}
