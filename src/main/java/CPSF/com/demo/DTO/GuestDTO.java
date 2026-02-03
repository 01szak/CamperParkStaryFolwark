package CPSF.com.demo.DTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;

public record GuestDTO (
     @Nullable String firstName,
     @Nullable String lastName,
     @Nullable @Email String email,
     @Nullable String phoneNumber,
     @Nullable String carRegistration,
     @Nullable String country,
     @Nullable String city,
     @Nullable String streetAddress
) {}
