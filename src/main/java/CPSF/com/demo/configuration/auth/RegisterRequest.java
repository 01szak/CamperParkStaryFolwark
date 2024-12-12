package CPSF.com.demo.configuration.auth;

import CPSF.com.demo.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String carRegistration;
    private String country;
    private String city;
    private String streetAddress;

    private String password;
}
