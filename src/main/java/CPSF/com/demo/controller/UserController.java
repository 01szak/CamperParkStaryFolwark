package CPSF.com.demo.controller;

import CPSF.com.demo.entity.DTO.UserDto;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.entity.DTO.UserRequest;
import CPSF.com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService theUserService) {
        userService = theUserService;
    }

    @GetMapping({"/getFilteredUsers/{value}", "/getFilteredUsers"})
    public List<UserDto> getFilteredUsers(@PathVariable(required = false) String value) {
        if(value != null && value.trim().isEmpty()) {
            value = null;
        }
        return userService.getFilteredUsers(value);
    }
    @GetMapping("/getAll")
    public List<UserDto> getAll() {
        return userService.findAllUsersDto();
    }
    @PatchMapping("updateUser/{id}")
    public void updateUser(@PathVariable int id,@RequestBody UserRequest request){
        userService.updateUser(id,request);
    }
}





