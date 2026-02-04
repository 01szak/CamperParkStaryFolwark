package CPSF.com.demo.DTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;

public record GuestDTO (
    @Nullable  Integer id,
    @Nullable String firstName,
    @Nullable String lastName,
    @Nullable @Email String email,
    @Nullable String phoneNumber,
    @Nullable String carRegistration
) {}
