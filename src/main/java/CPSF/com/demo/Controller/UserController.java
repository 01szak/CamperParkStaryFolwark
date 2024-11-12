package CPSF.com.demo.Controller;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Entity.Role;
import CPSF.com.demo.Entity.User;
import CPSF.com.demo.Repository.UserRepository;
import CPSF.com.demo.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService theUserService){
        userService = theUserService;
    }

    @GetMapping("/findAll")
    public List<User> findAll(){

        return userService.findAll() ;

    }
    @GetMapping("/getByReservationId/{camperPlaceId}")
    public User getUserByCamperPlaceId(@PathVariable int camperPlaceId){

        User user = userService.getUserByCamperPlaceId(camperPlaceId);
        return user;
    }
//   @PostMapping("/create")
//    public User createUser(User user, String firstName, String lastName, String email,
//                           String phoneNumber, String carRegistration, Role role,
//                           Reservation reservation, CamperPlace camperPlace){
//        return userService.createUser(
//                user,firstName,lastName,email,phoneNumber
//                ,carRegistration,reservation,camperPlace
//        );


   }





