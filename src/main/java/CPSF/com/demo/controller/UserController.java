package CPSF.com.demo.controller;

import CPSF.com.demo.exception.UserNotFoundException;
import CPSF.com.demo.model.dto.UserDTO;
import CPSF.com.demo.service.core.UserService;
import CPSF.com.demo.service.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDTO getEmployee(@AuthenticationPrincipal Jwt jwt) {
        var username = jwt.getClaimAsString("sub");

        return userService.findBy("login", username)
                .map(DtoMapper::getUserDTO)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Zalogowany użytkownik nie istnieje już w systemie"));
    }
}
