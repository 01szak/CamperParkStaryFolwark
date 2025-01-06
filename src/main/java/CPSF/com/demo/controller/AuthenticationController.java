package CPSF.com.demo.controller;

import CPSF.com.demo.EmailValidator;
import CPSF.com.demo.configuration.auth.AuthenticationRequest;
import CPSF.com.demo.configuration.auth.AuthenticationResponse;
import CPSF.com.demo.service.AuthenticationService;
import CPSF.com.demo.configuration.auth.RegisterRequest;
import CPSF.com.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private  final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        if(!EmailValidator.check(request.getEmail())){
            return ResponseEntity.badRequest().body(AuthenticationResponse.builder()
                    .message("Incorrect email")
                    .build());
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping ("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));

    }


}
