package CPSF.com.demo.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CamperPlace_DTO(
        @NotNull int id,
        @NotBlank String index,
        @NotBlank CamperPlaceTypeDTO type,
        @NotNull @Positive BigDecimal price,
        @Nullable List<Reservation_DTO> reservations
) {}
