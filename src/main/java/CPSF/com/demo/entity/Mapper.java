package CPSF.com.demo.entity;

import CPSF.com.demo.entity.DTO.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class Mapper {

//    @Autowired
//    private final PasswordEncoder passwordEncoder;
//
//

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
//
//
//    public User toUser(AuthDTO.RegisterRequest request) {
//        return User.builder()
//                .firstName(request.firstName())
//                .lastName(request.lastName())
//                .email(request.email())
//                .phoneNumber(request.phoneNumber())
//                .carRegistration(request.carRegistration())
//                .country(request.country())
//                .city(request.city())
//                .streetAddress(request.streetAddress())
//                .password(passwordEncoder.encode(request.streetAddress()))
//                .role(Role.GUEST)
//                .reservations(new ArrayList<Reservation>())
//                .build();
//
//    }
}

