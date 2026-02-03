package CPSF.com.demo.DTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record Reservation_DTO(
        @NotNull int id,
        @NotNull String checkin,
        @NotNull String checkout,
        @NotNull GuestDTO guest,
        @NotNull String camperPlaceIndex,
        @NotNull Boolean paid,
        @Nullable String reservationStatus
) {}
