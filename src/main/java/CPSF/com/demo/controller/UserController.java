package CPSF.com.demo.controller;

import CPSF.com.demo.DTO.UserDTO;
import CPSF.com.demo.service.UserService;
import CPSF.com.demo.util.DtoMapper;
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
        try {
            Authentication a = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) a.getPrincipal();
            String username = jwt.getClaimAsString("iss");
            return userService.findBy(null, "login", username)
                    .map(DtoMapper::getUserDTO)
                    .stream()
                    .toList()
                    .get(0);
//        TODO
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
