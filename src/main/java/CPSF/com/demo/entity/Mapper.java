package CPSF.com.demo.entity;

import CPSF.com.demo.entity.DTO.ReservationDto;
import CPSF.com.demo.entity.DTO.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

//    @Autowired
//    private final PasswordEncoder passwordEncoder;
//
//

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .carRegistration(user.getCarRegistration())
                .country(user.getCountry())
                .city(user.getCity())
                .streetAddress(user.getStreetAddress())
                .reservations(user.getReservations() )
                .build();

    }

    public ReservationDto toReservationDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .checkin(reservation.getCheckin())
                .checkout(reservation.getCheckout())
                .camperPlaceIndex(reservation.getCamperPlace().getIndex())
                .userFirstName(reservation.getUser().getFirstName())
                .userLastName(reservation.getUser().getLastName())
                .userEmail(reservation.getUser().getEmail())
                .reservationStatus(String.valueOf(reservation.getReservationStatus()))
                .paid(reservation.getPaid())
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

