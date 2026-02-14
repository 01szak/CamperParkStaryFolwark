package CPSF.com.demo.model.dto;


import CPSF.com.demo.model.entity.User.UserRole;

public class AuthDTO {
    public record LoginRequest(String login, String password) {
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
            UserRole userRole
    ) {
    }

    public record Response(String message, String token) {
    }
}