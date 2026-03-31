package CPSF.com.demo.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Reservation_DTO(
        @Nullable Integer id,
        @NotBlank String checkin,
        @NotBlank String checkout,
        @NotNull GuestDTO guest,
        @NotNull CamperPlace_DTO camperPlace,
        @NotNull Boolean paid,
        @Nullable String reservationStatus
) {}
