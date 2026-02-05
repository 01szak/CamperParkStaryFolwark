package CPSF.com.demo.model.dto;

import jakarta.validation.constraints.NotNull;

public record UserDTO(
        @NotNull String username,
        @NotNull String email,
        @NotNull String role
){}
