package CPSF.com.demo.service;

import exception.ClientInputException;
import CPSF.com.demo.entity.*;
import CPSF.com.demo.entity.DTO.ReservationDTO;
import CPSF.com.demo.entity.DTO.ReservationMetadataDTO;
import CPSF.com.demo.entity.DTO.ReservationRequest;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static exception.ClientInputExceptionUtil.ensure;

@Service
@AllArgsConstructor
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final CamperPlaceService camperPlaceService;
    private final ReservationMetadataService reservationMetadataService;

	@Transactional
    public Reservation createReservation(String checkin, String checkout, String camperPlaceIndex, User user) {

        ensureDataIsCorrect(checkin, checkout, camperPlaceIndex, user);

        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        CamperPlace camperPlace = camperPlaceService.findCamperPlaceByIndex(camperPlaceIndex);

        User u = userService.createUserIfDontExist(user);

        return reservationRepository.save(
            Reservation.builder()
                .checkin(checkinDate)
                .checkout(checkoutDate)
                .camperPlace(camperPlace)
                .user(u)
                .paid(false)
                .build()
        );
    }

    private void ensureDataIsCorrect(String checkin, String checkout, String camperPlaceIndex, User user) {

        ensure(checkin.isEmpty(), "Podaj datę wjazdu");
        ensure(checkout.isEmpty(), "Podaj datę wyjazdu");
        ensure(
                LocalDate.parse(checkin).isAfter(LocalDate.parse(checkout))
                        || LocalDate.parse(checkin).isEqual(LocalDate.parse(checkout)),
                "Wprowadzone daty są nieprawidłowe"
        );
        ensure(user == null, "Pole gościa nie może być puste");
        ensure(
                setToStringIfNull(user.getLastName()).isEmpty()
                        && setToStringIfNull(user.getFirstName()).isEmpty()
                        && setToStringIfNull(user.getPhoneNumber()).isEmpty(),
                "Podaj dane gościa"
        );
        ensure(
                camperPlaceIndex == null
                 || camperPlaceService.findCamperPlaceByIndex(camperPlaceIndex) == null,
                "Wybierz nr. parceli"
        );
        ensure(
                camperPlaceService.checkIsCamperPlaceOccupied(
                        camperPlaceService.findCamperPlaceByIndex(camperPlaceIndex),
                        LocalDate.parse(checkin),
                        LocalDate.parse(checkout),
                        0
                ),
                        "Parcela jest już zajęta!"
        );
    }


    @Transactional
    public Reservation deleteReservation(int id) {
        Reservation reservation = findReservationById(id);
        reservation.getCamperPlace().setReservations(null);
        reservationRepository.delete(reservation);
		reservationRepository.flush();
	    return reservation;
    }
    @Transactional
    public Reservation updateReservation(int id, ReservationRequest request) {

        CamperPlace camperPlace = request.camperPlaceIndex() == null ? null : camperPlaceService.findCamperPlaceByIndex(request.camperPlaceIndex());
        LocalDate checkin = request.checkin() == null ? null : LocalDate.parse(request.checkin());
        LocalDate checkout = request.checkout() == null ? null : LocalDate.parse(request.checkout());

        if (camperPlace != null && camperPlaceService.checkIsCamperPlaceOccupied(camperPlace, checkin, checkout, id)) {
            throw new ClientInputException("Parcela jest już zajęta!");
        }

        Reservation reservation = findReservationById(id);
        Optional.ofNullable(checkin).ifPresent(reservation::setCheckin);
        Optional.ofNullable(checkout).ifPresent(reservation::setCheckout);
        Optional.ofNullable(camperPlace).ifPresent(reservation::setCamperPlace);
		//            statisticsService.update(reservation);
		Optional.ofNullable(request.paid()).ifPresent(reservation::setPaid);
        if (!reservation.isCheckoutAfterCheckin()) {
            throw new ClientInputException("Data wyjazdu nie może być przed datą wjazdu");
        }
        return reservationRepository.save(reservation);
    }
    @Transactional
    public void updateReservationStatus(Reservation reservation) {

        if (isLocalDateInBetweenCheckinAndCheckout(reservation.getCheckin(), reservation.getCheckout())) {
            reservation.setReservationStatus(ReservationStatus.ACTIVE);
        } else if (!isLocalDateInBetweenCheckinAndCheckout(reservation.getCheckin(), reservation.getCheckout()) && LocalDate.now().isBefore(reservation.getCheckin())) {
            reservation.setReservationStatus(ReservationStatus.COMING);
        } else {
            reservation.setReservationStatus(ReservationStatus.EXPIRED);
        }
        reservationRepository.save(reservation);
    }

    public  List<Reservation> findByMonthYearAndCamperPlaceId(int month, int year, CamperPlace camperPlace) {
        return reservationRepository.findByMonthYearAndCamperPlace(month, year, camperPlace.getId());
    }

    public Map<String, ReservationMetadataDTO> getReservationMetadataDTO() {
        return reservationMetadataService.getReservationMetaDataDTO();
    }

    public List<ReservationDTO> findAllReservationsDto() {
        return reservationRepository.findAll().stream().map(Mapper::toReservationDTO).toList();
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation findReservationById(int id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    public List<ReservationDTO> getFilteredData(String value) {
        if (value == null) {
            return findAllReservationsDto();
        }
        List<ReservationDTO> allReservationsDto = findAllReservationsDto();

        List<ReservationDTO> filteredList = new ArrayList<>();
        String filterValue = value.toLowerCase();
        allReservationsDto.forEach(reservationDto -> {
            if (reservationDto.getCheckin().toString().contains(filterValue) ||
                    reservationDto.getCheckout().toString().contains(filterValue) ||
                    reservationDto.getUser().getFirstName().toLowerCase().contains(filterValue) ||
                    reservationDto.getUser().getLastName().toLowerCase().contains(filterValue) ||
                    reservationDto.getReservationStatus().toLowerCase().contains(filterValue) ||
                    (isNumber(value) && reservationDto.getCamperPlaceIndex().equals(value))

            ) {
                filteredList.add(reservationDto);
            }

        });
        return filteredList;
    }

    private boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public List<ReservationDTO> getSortedReservations(String sortedHeader, int isAsc) {
        if (isAsc == 1) {
            return sortAsc(sortedHeader);
        }
        return sortDesc(sortedHeader);
    }

    private boolean isLocalDateInBetweenCheckinAndCheckout(LocalDate checkin, LocalDate checkout) {
        return (LocalDate.now().isEqual(checkin) || LocalDate.now().isAfter(checkin))
                && (LocalDate.now().isEqual(checkout) || LocalDate.now().isBefore(checkout));
    }

    private List<ReservationDTO> sortAsc(String sortedHeader) {
        switch (sortedHeader) {
            case "checkin" -> {
                return reservationRepository.orderByCheckinAsc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "checkout" -> {
                return reservationRepository.orderByCheckoutAsc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "guest" -> {
                return reservationRepository.orderByLastNameAsc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "number" -> {
                return reservationRepository.orderByCamperPlaceNumberAsc().stream().map(Mapper::toReservationDTO).toList();
            }
            default -> {
                return findAllReservationsDto();
            }
        }
    }

    private List<ReservationDTO> sortDesc(String sortedHeader) {
        switch (sortedHeader) {
            case "checkin" -> {
                return reservationRepository.orderByCheckinDesc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "checkout" -> {
                return reservationRepository.orderByCheckoutDesc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "guest" -> {
                return reservationRepository.orderByLastNameDesc().stream().map(Mapper::toReservationDTO).toList();
            }
            case "number" -> {
                return reservationRepository.orderByCamperPlaceNumberDesc().stream().map(Mapper::toReservationDTO).toList();
            }
            default -> {
                return findAllReservationsDto();
            }
        }
    }
    private String setToStringIfNull(String s) {
		return s == null ? "" : s;
	}

}

