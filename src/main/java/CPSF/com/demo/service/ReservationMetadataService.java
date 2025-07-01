package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.DTO.ReservationMetadataDTO;
import CPSF.com.demo.entity.Mapper;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.ReservationMetadata;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationMetadataService {

	private final CamperPlaceService camperPlaceService;

	public Map<String, ReservationMetadataDTO> getReservationMetaDataDTO() {
		Map<String, ReservationMetadataDTO> metadataMap = new HashMap<>();
		List<CamperPlace> camperPlaces = camperPlaceService.getAll();

		for (CamperPlace cp : camperPlaces) {
			metadataMap.put(cp.getIndex(), assignReservationMetadata(cp));
		}
		return metadataMap;
	}

	private ReservationMetadataDTO assignReservationMetadata(CamperPlace cp) {
		Set<String> reserved = new HashSet<>();
	 	Set<String> checkin = new HashSet<>();
	 	Set<String> checkout = new HashSet<>();
	 	List<Reservation> reservations = cp.getReservations();
		Set<LocalDate> allDates = new HashSet<>();
		 if (reservations == null || reservations.isEmpty()) {
			 return new ReservationMetadataDTO();
		 }

		for (Reservation r : reservations) {
			Set<LocalDate>dates = r.getCheckin().datesUntil(r.getCheckout().plusDays(1))
					.collect(Collectors.toSet());
			allDates.addAll(dates);
		}

		for (LocalDate d : allDates) {
			reserved.add(d.toString());
			if (!allDates.contains(d.minusDays(1))) {
				checkin.add(d.toString());
			}
			if (!allDates.contains(d.plusDays(1))) {
				checkout.add(d.toString());
			}
		}

		ReservationMetadata reservationMetadata = new ReservationMetadata();
		reservationMetadata.addReserved(reserved);
		reservationMetadata.addCheckin(checkin);
		reservationMetadata.addCheckout(checkout);

		return Mapper.toReservationMetadataDTO(reservationMetadata);
	}

}
