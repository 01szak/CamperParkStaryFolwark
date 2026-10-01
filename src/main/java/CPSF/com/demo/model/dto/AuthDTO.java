package CPSF.com.demo.model.dto;

public class AuthDTO {
    public record LoginRequest(String login, String password) {
    }

    public record Response(String message, String token) {
    }
}