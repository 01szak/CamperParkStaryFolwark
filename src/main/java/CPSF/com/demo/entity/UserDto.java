package CPSF.com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<Reservation> reservations;
    private String carRegistration;
    private String country;
    private String city;
    private String streetAddress;


}
