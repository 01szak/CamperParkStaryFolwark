package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.request.UserRequest;
import CPSF.com.demo.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl theUserService) {
        userService = theUserService;
    }

    @GetMapping({"/getFilteredUsers/{value}", "/getFilteredUsers"})
    public List<UserDTO> getFilteredUsers(@PathVariable(required = false) String value) {
        if(value != null && value.trim().isEmpty()) {
            value = null;
        }
        return userService.getFilteredUsers(value);
    }
    @GetMapping("/getAll")
    public List<UserDTO> getAll() {
        return userService.findAllUsersDto();
    }
    @PatchMapping("/updateUser/{id}")
    public void updateUser(@PathVariable int id,@RequestBody UserRequest request){
        userService.updateUser(id,request);
    }
    @GetMapping("/getUser/{id}")
    public UserDTO getUserById(@PathVariable int id){
        return userService.findUserDtoById(id);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id){
         userService.deleteUser(id);
    }
}





