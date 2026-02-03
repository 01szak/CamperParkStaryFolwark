package CPSF.com.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CamperPlace_DTO(
        @NotNull int id,
        @NotBlank String index,
        @NotBlank String type,
        @NotNull @Positive BigDecimal price
) {}
