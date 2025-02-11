package CPSF.com.demo.A_security.module;

import CPSF.com.demo.enums.Role;

public class AuthDTO {
    public record LoginRequest(String email, String password) {
    }

    public record RegisterRequest(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String carRegistration,
            String country,
            String city,
            String streetAddress,
            String password,
            Role role
    ) {
    }

    public record Response(String message, String token) {
    }
}