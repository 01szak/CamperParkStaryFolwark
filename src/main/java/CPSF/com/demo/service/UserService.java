package CPSF.com.demo.service;

import CPSF.com.demo.entity.User;
import CPSF.com.demo.entity.UserDTO;
import CPSF.com.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
   private final UserRepository userRepository;
   private final  CamperPlaceService camperPlaceService;
   private final ReservationService reservationService;

    public UserService(UserRepository userRepository, CamperPlaceService camperPlaceService, ReservationService reservationService) {
        this.userRepository = userRepository;
        this.camperPlaceService = camperPlaceService;
        this.reservationService = reservationService;
    }

    public List<User> findAll(){
        return userRepository.findAll() ;

    }

//TODO: create UserDto

//  public UserDTO createUser(
//          User user,String firstName,
//          String lastName,
//          String email,
//          String phoneNumber,
//          String carRegistration,
//          Reservation reservation,
//          CamperPlace camperPlace,int camperPlaceNumber, LocalDate enter, LocalDate checkout){
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setEmail(email);
//        user.setPhoneNumber(phoneNumber);
//        user.setRole(new Role(2,"guest"));
//
//         reservationService.createReservation(camperPlaceNumber,enter,checkout);
//        user.setCamperPlace(camperPlace);
//        return userRepository.save(user);
//  }

}
