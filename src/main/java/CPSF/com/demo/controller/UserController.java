package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.request.UserRequest;
import CPSF.com.demo.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl theUserService) {
        userService = theUserService;
    }


    @GetMapping
    public Page<UserDTO> findAll(Pageable pageable,
                                 @RequestParam(required = false) String by,
                                 @RequestParam(required = false) String value)
            throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        if (by != null && value != null) {
            return userService.findDTOBy(pageable, by, value);
        }
        return userService.findAllDTO(pageable);
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody UserRequest request){
        userService.update(id,request);
    }

    @PostMapping
    public void create(@RequestBody UserRequest request) {
        userService.create(request);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable int id){
        return userService.findDTOById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id){
         userService.delete(id);
    }
}





