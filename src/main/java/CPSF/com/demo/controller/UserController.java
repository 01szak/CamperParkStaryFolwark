package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.UserDTO;
import CPSF.com.demo.service.core.UserService;
import CPSF.com.demo.service.util.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public UserDTO getEmployee() {
            Authentication a = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) a.getPrincipal();
            String username = jwt.getClaimAsString("iss");
            return userService.findBy("login", username)
                    .map(DtoMapper::getUserDTO)
                    .stream()
                    .toList()
                    .get(0);
    }
}
