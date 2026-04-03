package CPSF.com.demo.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record Reservation_DTO(
        @Nullable Integer id,
        @NotNull LocalDate checkin,
        @NotNull LocalDate checkout,
        @NotNull GuestDTO guest,
        @NotNull CamperPlace_DTO camperPlace,
        @NotNull Boolean paid,
        @Nullable String reservationStatus
) {}
