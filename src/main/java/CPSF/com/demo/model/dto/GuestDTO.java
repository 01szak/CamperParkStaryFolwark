package CPSF.com.demo.model.dto;

import CPSF.com.demo.service.validator.CountryIso;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;


public record GuestDTO (
    @Nullable Integer id,
    @Nullable String firstname,
    @Nullable String lastname,
    @Nullable @Email String email,
    @Nullable String phoneNumber,
    @Nullable String carRegistration,
    @Nullable @CountryIso String country
) {}
