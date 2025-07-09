package CPSF.com.demo.service;

import CPSF.com.demo.entity.*;
import CPSF.com.demo.entity.DTO.ReservationMetadataDTO;
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

	public Map<String, PaidReservations> getPaidReservations() {
		Map<String, PaidReservations> metadataMap = new HashMap<>();
		List<CamperPlace> camperPlaces = camperPlaceService.getAll();

		for(CamperPlace cp : camperPlaces) {
			metadataMap.put(cp.getIndex(), assignReservationIfPaidOrNotPaid(true, cp));
		}

		return metadataMap;
	}

	public Map<String, PaidReservations> getUnPaidReservations() {
		Map<String, PaidReservations> metadataMap = new HashMap<>();
		List<CamperPlace> camperPlaces = camperPlaceService.getAll();

		for(CamperPlace cp : camperPlaces) {
			metadataMap.put(cp.getIndex(), assignReservationIfPaidOrNotPaid(false, cp));
		}

		return metadataMap;
	}

	public Map<String, Map<String,Set<String>>> getUserPerReservation() {
		UserPerReservation metadataMap = new UserPerReservation();
		List<CamperPlace> camperPlaces = camperPlaceService.getAll();

		for (CamperPlace cp: camperPlaces) {
				metadataMap.setUserPerReservation(assignUserPerReservation(cp));
				metadataMap.addCamperPlaceToUserPerReservation(cp.getIndex());
		}

		return metadataMap.getCamperPlacePerUserPerReservation();
	}

	private Map<String, Set<String>> assignUserPerReservation(CamperPlace cp) {
		Map<String, Set<String>> userPerReservation = new HashMap<>();

		for (Reservation r : cp.getReservations()) {
			userPerReservation.put(
					r.getUser().toString(), mapReservationDatesToString(r)
			);
		}

		return userPerReservation;
	}

	private PaidReservations assignReservationIfPaidOrNotPaid(boolean paid, CamperPlace cp) {
		List<Reservation> reservations = cp.getReservations();
		PaidReservations paidReservations = new PaidReservations();

		if (reservations == null) {
			return new PaidReservations();
		}

		for(Reservation r : reservations) {
			if (paid) {
				if (r != null && r.getPaid()) {
					paidReservations.addDates(mapReservationDatesToString(r));
				}
			} else {
				if (r != null && !r.getPaid()) {
					paidReservations.addDates(mapReservationDatesToString(r));
				}
			}
		}

		return paidReservations;
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
		reserved.addAll(mapReservationDatesToString(allDates));

		ReservationMetadata reservationMetadata = new ReservationMetadata();
		reservationMetadata.addReserved(reserved);
		reservationMetadata.addCheckin(checkin);
		reservationMetadata.addCheckout(checkout);

		return Mapper.toReservationMetadataDTO(reservationMetadata);
	}

	private Set<String> mapReservationDatesToString(Reservation r) {
		return r.getCheckin().datesUntil(r.getCheckout().plusDays(1))
				.map(LocalDate::toString)
				.collect(Collectors.toSet());
	}

	private Set<String> mapReservationDatesToString(Set<LocalDate> dates) {
		return dates.stream()
				.map(LocalDate::toString)
				.collect(Collectors.toSet());
	}
}
