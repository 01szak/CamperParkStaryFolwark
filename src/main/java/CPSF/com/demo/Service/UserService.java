package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Entity.Role;
import CPSF.com.demo.Entity.User;
import CPSF.com.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

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

    public User getUserByCamperPlaceId(int camperPlaceId){
        return userRepository.findUserByCamperPlace_Id(camperPlaceId);
    }
  public User createUser(User user,String firstName, String lastName,String email,String phoneNumber, String carRegistration,Reservation reservation,CamperPlace camperPlace,int camperPlaceNumber, LocalDate enter, LocalDate checkout){
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setRole(new Role(2,"guest"));

         reservationService.createReservation(camperPlaceNumber,enter,checkout);
        user.setCamperPlace(camperPlace);
        return userRepository.save(user);
  }

}
