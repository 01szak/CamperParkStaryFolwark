package CPSF.com.demo.DTO;

import CPSF.com.demo.enums.CamperPlaceType;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CamperPlaceDTO extends DTO{
	private int id;
	private String index;
	private Boolean isOccupied;
	private CamperPlaceType camperPlaceType;
	private double price;
	private List<ReservationDTO> reservations;
}
