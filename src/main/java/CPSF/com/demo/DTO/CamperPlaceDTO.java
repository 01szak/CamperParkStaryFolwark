package CPSF.com.demo.DTO;

import CPSF.com.demo.enums.Type;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CamperPlaceDTO {
	private int id;
	private String index;
	private Boolean isOccupied;
	private Type type;
	private double price;
	private List<ReservationDTO> reservations;
}
