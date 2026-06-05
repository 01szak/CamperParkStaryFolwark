package CPSF.com.demo.model.dto;

import CPSF.com.demo.model.constant.ReservationStatus;
import CPSF.com.demo.model.entity.Reservation;
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
        @Nullable ReservationStatus reservationStatus
) {}
