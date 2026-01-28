package CPSF.com.demo.DTO;

import java.math.BigDecimal;

public record CamperPlace_DTO(
        int id,
        String index,
        String type,
        BigDecimal price
) {}
