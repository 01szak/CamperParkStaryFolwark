package CPSF.com.demo.A_security.module;

import CPSF.com.demo.enums.EmployeeRole;

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
            EmployeeRole employeeRole
    ) {
    }

    public record Response(String message, String token) {
    }
}