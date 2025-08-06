package CPSF.com.demo.DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserPerReservationDTO extends DTO {
	private Map<String, List<String>> userPerReservation = new HashMap<>();
	private Map<String, Map<String,List<String>>> camperPlacePerUserPerReservation = new HashMap<>();

	public void addDates(String userInfo, List<String> dates) {
		userPerReservation.put(userInfo, dates);
	}

	public void addCamperPlaceToUserPerReservation(String camperPlaceIndex) {
		camperPlacePerUserPerReservation.put(camperPlaceIndex,userPerReservation);
	}

}

