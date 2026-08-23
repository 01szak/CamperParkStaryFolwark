package CPSF.com.demo.model.dto;

import CPSF.com.demo.model.constant.ReservationStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record ReservationDTO(
        @Nullable Integer id,
        @NotNull LocalDate checkin,
        @NotNull LocalDate checkout,
        @NotNull GuestDTO guest,
        @NotNull camperPlaceDTO camperPlace,
        @NotNull Boolean paid,
        @Nullable ReservationStatus reservationStatus
) {}
