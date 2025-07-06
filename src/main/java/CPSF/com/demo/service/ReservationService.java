package CPSF.com.demo.service;

import CPSF.com.demo.ClientInputException;
import CPSF.com.demo.entity.*;
import CPSF.com.demo.entity.DTO.ReservationDTO;
import CPSF.com.demo.entity.DTO.ReservationMetadataDTO;
import CPSF.com.demo.entity.DTO.ReservationRequest;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class ReservationService {

     ReservationRepository reservationRepository;
     UserService userService;
     CamperPlaceService camperPlaceService;
     StatisticsService statisticsService;
     ReservationMetadataService reservationMetadataService;

    @Transactional
    public void createReservation(String checkin, String checkout, String camperPlaceIndex, User user) {
        CamperPlace camperPlace = camperPlaceService.findCamperPlaceByIndex(camperPlaceIndex);

        if ((user.getLastName().isEmpty() && user.getFirstName().isEmpty()) || (user.getEmail().isEmpty() && user.getPhoneNumber().isEmpty())) {
            throw new ClientInputException("Empty Guest Form");
        }
        User u = userService.createUserIfDontExist(user);
        if (camperPlace == null) {
            throw new ClientInputException("Camper Place Form Is Empty");
        }
        try {
            if (camperPlaceService.checkIsCamperPlaceOccupied(camperPlace, LocalDate.parse(checkin), LocalDate.parse(checkout), 0)) {
                throw new ClientInputException("Camper Place Is Already Occupied");
            } else {
                reservationRepository.save(Reservation.builder()
                        .checkin(LocalDate.parse(checkin))
                        .checkout(LocalDate.parse(checkout))
                        .camperPlace(camperPlace)
                        .user(u)
                        .paid(false)
                        .build()
                );
            }
        } catch (ConstraintViolationException e) {
            throw new ClientInputException("Can't Checkout Before Checkin");
        }
    }
    @Transactional
    public void deleteReservation(int id) {
        Reservation reservation = findReservationById(id);
        reservation.getCamperPlace().setReservations(null);
        reservationRepository.delete(reservation);
        reservationRepository.flush();
    }
    @Transactional
    public void updateReservation(int id, ReservationRequest request) {
        CamperPlace camperPlace = request.camperPlaceIndex() == null ? null : camperPlaceService.findCamperPlaceByIndex(request.camperPlaceIndex());
        LocalDate checkin = request.checkin() == null ? null : LocalDate.parse(request.checkin());
        LocalDate checkout = request.checkout() == null ? null : LocalDate.parse(request.checkout());

        if (camperPlace != null && camperPlaceService.checkIsCamperPlaceOccupied(camperPlace, checkin, checkout, id)) {
            throw new ClientInputException("Camper Place Is Already Occupied");
        }

        Reservation reservation = findReservationById(id);
        Optional.ofNullable(checkin).ifPresent(reservation::setCheckin);
        Optional.ofNullable(checkout).ifPresent(reservation::setCheckout);
        Optional.ofNullable(camperPlace).ifPresent(reservation::setCamperPlace);
        Optional.ofNullable(request.paid()).ifPresent(paid -> {
            reservation.setPaid(paid);
            statisticsService.update(reservation);
        });
        if (!reservation.isCheckoutAfterCheckin()) {
            throw new ClientInputException("Can't Checkout Before Checkin!!");
        }
        reservationRepository.save(reservation);
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

}

