package CPSF.com.demo.Controller;

import CPSF.com.demo.Entity.User;
import CPSF.com.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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



   }





