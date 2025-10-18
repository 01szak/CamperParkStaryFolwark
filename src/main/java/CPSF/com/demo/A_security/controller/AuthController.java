package CPSF.com.demo.A_security.controller;

import CPSF.com.demo.A_security.module.AuthDTO;
import CPSF.com.demo.A_security.service.JwtServiceImpl;
import CPSF.com.demo.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService service;

    public AuthController(AuthenticationManager authenticationManager, JwtServiceImpl jwtTokenServiceImpl, JwtService service) {
        this.authenticationManager = authenticationManager;
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest credentials) throws IllegalAccessException {

        UsernamePasswordAuthenticationToken a =
                new UsernamePasswordAuthenticationToken(credentials.login(), credentials.password());

        Authentication authentication = authenticationManager.authenticate(a);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = service.generateToken(authentication);
        return ResponseEntity.ok(new AuthDTO.Response("Authenticated", token));
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody AuthDTO.RegisterRequest request) throws SQLIntegrityConstraintViolationException {
//        User user;
//        userService.create(
//                user = User.builder()
//                        .firstName(request.firstName())
//                        .lastName(request.lastName())
//                        .email(request.email())
//                        .phoneNumber(request.phoneNumber())
//                        .carRegistration(request.carRegistration())
//                        .country(request.country())
//                        .city(request.city())
//                        .streetAddress(request.streetAddress())
//                        .password(request.password())
//                        .reservations(new ArrayList<Reservation>())
//                        .employeeRole(EmployeeRole.GUEST)
//                        .build()
//        );
//        UsernamePasswordAuthenticationToken a = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//        Authentication authentication = authenticationManager.authenticate(a);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = jwtTokenService.generateToken(authentication);
//        return ResponseEntity.ok(new AuthDTO.Response("Registered", token));
//
//    }


}
