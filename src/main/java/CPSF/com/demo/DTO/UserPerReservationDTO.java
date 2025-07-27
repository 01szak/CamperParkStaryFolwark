package CPSF.com.demo.DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class UserPerReservationDTO extends DTO {
	private Map<String, Set<String>> userPerReservation = new HashMap<>();
	private Map<String, Map<String,Set<String>>> camperPlacePerUserPerReservation = new HashMap<>();

	public void addDates(String userInfo, Set<String> dates) {
		userPerReservation.put(userInfo, dates);
	}

	public void addCamperPlaceToUserPerReservation(String camperPlaceIndex) {
		camperPlacePerUserPerReservation.put(camperPlaceIndex,userPerReservation);
	}

}

