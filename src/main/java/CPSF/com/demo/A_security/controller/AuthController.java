package CPSF.com.demo.A_security.controller;

import CPSF.com.demo.A_security.module.AuthDTO;
import CPSF.com.demo.A_security.service.JwtTokenService;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.enums.Role;
import CPSF.com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;


@RestController()
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest credentials) throws IllegalAccessException {
        UsernamePasswordAuthenticationToken a = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());
        Authentication authentication = authenticationManager.authenticate(a);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String token = jwtTokenService.generateToken(authentication);

        return ResponseEntity.ok(new AuthDTO.Response("Authenticated", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDTO.RegisterRequest request) throws SQLIntegrityConstraintViolationException {
        User user = new User(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phoneNumber(),
                request.carRegistration(),
                request.country(),
                request.city(),
                request.streetAddress(),
                request.password(),
                Role.GUEST,
                new ArrayList<Reservation>()
        );
        userService.save(user);


        UsernamePasswordAuthenticationToken a = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(a);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenService.generateToken(authentication);
        return ResponseEntity.ok(new AuthDTO.Response("Registered", token));

    }


}
