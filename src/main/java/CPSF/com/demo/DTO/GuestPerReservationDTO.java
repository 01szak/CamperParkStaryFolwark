package CPSF.com.demo.DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GuestPerReservationDTO extends DTO {
	private Map<String, List<String>> guestPerReservation = new HashMap<>();
	private Map<String, Map<String,List<String>>> camperPlacePerUserPerReservation = new HashMap<>();

	public void addDates(String guest, List<String> dates) {
		guestPerReservation.put(guest, dates);
	}

	public void addCamperPlaceToUserPerReservation(String camperPlaceIndex) {
		camperPlacePerUserPerReservation.put(camperPlaceIndex, guestPerReservation);
	}

}

