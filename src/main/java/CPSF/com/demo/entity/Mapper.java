package CPSF.com.demo.entity;

import CPSF.com.demo.configuration.auth.RegisterRequest;
import CPSF.com.demo.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Mapper {
private final PasswordEncoder passwordEncoder;

    public Mapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .carRegistration(user.getCarRegistration())
                .country(user.getCountry())
                .city(user.getCity())
                .streetAddress(user.getStreetAddress())
                .reservations(new ArrayList<Reservation>())
                .build();

    }


    public User toUser(RegisterRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .carRegistration(request.getCarRegistration())
                .country(request.getCountry())
                .city(request.getCity())
                .streetAddress(request.getStreetAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.GUEST)
                .reservations(new ArrayList<Reservation>())
                .build();

    }
}

