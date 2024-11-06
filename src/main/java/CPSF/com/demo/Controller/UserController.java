package CPSF.com.demo.Controller;

import CPSF.com.demo.Entity.User;
import CPSF.com.demo.Repository.UserRepository;
import CPSF.com.demo.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
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



}
