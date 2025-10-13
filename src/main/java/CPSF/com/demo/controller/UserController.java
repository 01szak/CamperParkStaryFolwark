package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.request.UserRequest;
import CPSF.com.demo.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<Map<String, String>> update(@PathVariable int id, @RequestBody UserRequest request){
        userService.update(id,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Gość został zmieniony"));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody UserRequest request) {
        userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Gość został utworzony"));
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable int id){
        return userService.findDTOById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable int id){
         userService.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success","Gość został usunięty"));
    }
}





