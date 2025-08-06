package CPSF.com.demo.util;

import CPSF.com.demo.DTO.PaidReservationsDTO;
import CPSF.com.demo.DTO.ReservationMetadataDTO;
import CPSF.com.demo.DTO.ReservationReservedCheckinCheckoutDTO;
import CPSF.com.demo.DTO.UserPerReservationDTO;
import CPSF.com.demo.entity.*;
import CPSF.com.demo.service.CamperPlaceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationMetadataMapper {

	private final CamperPlaceService camperPlaceService;

	public Map<String, ReservationMetadataDTO> getReservationMetaDataDTO() {
		Map<String, ReservationMetadataDTO> metadataMap = new HashMap<>();
		List<CamperPlace> camperPlaces = camperPlaceService.findAll(null).toList();

		for (CamperPlace cp : camperPlaces) {
			metadataMap.put(cp.getIndex(), assignReservationMetadata(cp));
		}

		return metadataMap;
	}

	public Map<String, PaidReservationsDTO> getPaidReservations() {
		Map<String, PaidReservationsDTO> metadataMap = new HashMap<>();
		List<CamperPlace> camperPlaces = camperPlaceService.findAll(null).toList();

		for(CamperPlace cp : camperPlaces) {
			metadataMap.put(cp.getIndex(), assignReservationIfPaidOrNotPaid(true, cp));
		}

		return metadataMap;
	}

	public Map<String, PaidReservationsDTO> getUnPaidReservations() {
		Map<String, PaidReservationsDTO> metadataMap = new HashMap<>();
		List<CamperPlace> camperPlaces = camperPlaceService.findAll(null).toList();

		for(CamperPlace cp : camperPlaces) {
			metadataMap.put(cp.getIndex(), assignReservationIfPaidOrNotPaid(false, cp));
		}

		return metadataMap;
	}

	public Map<String, Map<String,List<String>>> getUserPerReservation() {
		UserPerReservationDTO metadataMap = new UserPerReservationDTO();
		List<CamperPlace> camperPlaces = camperPlaceService.findAll(null).toList();

		for (CamperPlace cp: camperPlaces) {
				metadataMap.setUserPerReservation(assignUserPerReservation(cp));
				metadataMap.addCamperPlaceToUserPerReservation(cp.getIndex());
		}

		return metadataMap.getCamperPlacePerUserPerReservation();
	}

	private Map<String, List<String>> assignUserPerReservation(CamperPlace cp) {
		Map<String, List<String>> userPerReservation = new HashMap<>();
		for (Reservation r : cp.getReservations()) {
			List<String> dates = mapReservationDatesToString(r);
			dates.set(0, dates.get(0) + " in");
			dates.set(dates.size() - 1, dates.get(dates.size() - 1) + " out");
					userPerReservation.put(
					r.getUser().toString(), dates
			);
		}

		return userPerReservation;
	}

	private PaidReservationsDTO assignReservationIfPaidOrNotPaid(boolean paid, CamperPlace cp) {
		List<Reservation> reservations = cp.getReservations();
		PaidReservationsDTO paidReservations = new PaidReservationsDTO();

		if (reservations == null) {
			return new PaidReservationsDTO();
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

		ReservationReservedCheckinCheckoutDTO reservationMetadata = new ReservationReservedCheckinCheckoutDTO();
		reservationMetadata.addReserved(reserved);
		reservationMetadata.addCheckin(checkin);
		reservationMetadata.addCheckout(checkout);

		return Mapper.toReservationMetadataDTO(reservationMetadata);
	}

	private List<String> mapReservationDatesToString(Reservation r) {
		return r.getCheckin().datesUntil(r.getCheckout().plusDays(1))
				.map(LocalDate::toString)
				.collect(Collectors.toList());
	}

	private List<String> mapReservationDatesToString(Set<LocalDate> dates) {
		return dates.stream()
				.map(LocalDate::toString)
				.collect(Collectors.toList());
	}
}
