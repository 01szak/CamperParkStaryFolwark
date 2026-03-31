package CPSF.com.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CamperPlace_DTO(
        @NotNull int id,
        @NotBlank String index,
        @NotNull CamperPlaceTypeDTO type,
        @NotNull @Positive BigDecimal price
) {}
