package CPSF.com.demo.service;

import CPSF.com.demo.configuration.auth.AuthenticationRequest;
import CPSF.com.demo.configuration.auth.AuthenticationResponse;
import CPSF.com.demo.configuration.auth.RegisterRequest;
import CPSF.com.demo.entity.Mapper;
import CPSF.com.demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Mapper mapper;
    public AuthenticationResponse register(RegisterRequest request) {
        User user = mapper.toUser(request);
        userService.save(user);
        var jwtToken = jwtService.generateToken(user);
        mapper.toDto(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userService.findUserByEmailForAuthenticationPurpose(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }


}
