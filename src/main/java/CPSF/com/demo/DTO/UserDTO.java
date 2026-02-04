package CPSF.com.demo.DTO;

import jakarta.validation.constraints.NotNull;

public record UserDTO(
        @NotNull String username,
        @NotNull String email,
        @NotNull String role
){}
