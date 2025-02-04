package CPSF.com.demo.entity.DTO;

import CPSF.com.demo.entity.Reservation;
import jakarta.persistence.Entity;
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
