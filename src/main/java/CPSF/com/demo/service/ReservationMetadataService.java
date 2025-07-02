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
			checkin.add(r.getCheckin().toString());
			checkout.add(r.getCheckout().toString());
		}
		reserved.addAll(allDates.stream()
				.map(d -> d.toString())
				.collect(Collectors.toSet()));

		ReservationMetadata reservationMetadata = new ReservationMetadata();
		reservationMetadata.addReserved(reserved);
		reservationMetadata.addCheckin(checkin);
		reservationMetadata.addCheckout(checkout);

		return Mapper.toReservationMetadataDTO(reservationMetadata);
	}

}
