package CPSF.com.demo.service;

import CPSF.com.demo.entity.User;
import CPSF.com.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public void save(User user){
        userRepository.save(user);
    }
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow();
    }


//TODO: create UserDto


}
