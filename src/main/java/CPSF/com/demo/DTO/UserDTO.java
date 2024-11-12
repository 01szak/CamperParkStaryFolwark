package CPSF.com.demo.DTO;

import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Entity.Role;
import lombok.Getter;

public record UserDTO(
        int id,
        String firstName,
        String LastName,
        String email,
        String phoneNumber,
        String carRegistration,
        Role role,
        Reservation reservation,
        CamperPlace camperPlace
) {
}
