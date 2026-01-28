package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final UserService userService;

    public EmployeeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserDTO getEmployee() {
        Authentication a =  SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) a.getPrincipal();
        String username = jwt.getClaimAsString("iss");
        return userService.getUserDTO(username);
    }
}
