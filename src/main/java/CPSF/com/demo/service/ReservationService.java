package CPSF.com.demo.service;

import CPSF.com.demo.ClientInputException;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.DTO.ReservationDto;
import CPSF.com.demo.entity.DTO.ReservationRequest;
import CPSF.com.demo.entity.Mapper;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserService userService;
    @Autowired
    CamperPlaceService camperPlaceService;
    @Autowired
    Mapper mapper;
    @Autowired
    StatisticsService statisticsService;

    @Transactional
    public void createReservation(LocalDate checkin, LocalDate checkout, CamperPlace camperPlace, User user) {
        CamperPlace cp;

        if ((user.getLastName().isEmpty() && user.getFirstName().isEmpty()) || (user.getEmail().isEmpty() && user.getPhoneNumber().isEmpty())) {
            throw new ClientInputException("Empty Guest Form");
        }

        User u = userService.createUserIfDontExist(user);

        if (camperPlace == null) {
            throw new ClientInputException("Camper Place Form Is Empty");
        } else {
            cp = camperPlaceService.findById(camperPlace.getId());

        }

        try {
            if (camperPlaceService.checkIsCamperPlaceOccupied(camperPlace, checkin, checkout, 0)) {
                throw new ClientInputException("Camper Place Is Already Occupied");
            } else {
                System.out.println("Reservation being saved: " + Reservation.builder()
                        .checkin(checkin)
                        .checkout(checkout)
                        .camperPlace(cp)
                        .user(u)
                        .paid(false)
                        .build()
                );

                reservationRepository.save(Reservation.builder()
                        .checkin(checkin)
                        .checkout(checkout)
                        .camperPlace(cp)
                        .user(u)
                        .paid(false)
                        .build()
                );
            }
        } catch (ConstraintViolationException e) {

            throw new ClientInputException("Can't Checkout Before Checkin");

        }

    }


    public List<ReservationDto> findAllReservationsDto() {
        return reservationRepository.findAll().stream().map(mapper::toReservationDto).toList();
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation findReservationById(int id) {
        return reservationRepository.findById(id).orElseThrow();
    }



    @Transactional
    public void deleteReservation(int id) {
        Reservation reservation = findReservationById(id);
        reservation.getCamperPlace().setReservations(null);
        reservationRepository.delete(reservation);
        reservationRepository.flush();

    }


    public List<ReservationDto> getFilteredData(String value) {

        if (value == null) {
            return findAllReservationsDto();
        }
        List<ReservationDto> allReservationsDto = findAllReservationsDto();

        List<ReservationDto> filteredList = new ArrayList<>();
        String filterValue = value.toLowerCase();
        allReservationsDto.forEach(reservationDto -> {
            if (reservationDto.getCheckin().toString().contains(filterValue) ||
                    reservationDto.getCheckout().toString().contains(filterValue) ||
                    reservationDto.getUserFirstName().toLowerCase().contains(filterValue) ||
                    reservationDto.getUserLastName().toLowerCase().contains(filterValue) ||
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

    public List<ReservationDto> getSortedReservations(String sortedHeader, int isAsc) {
        if (isAsc == 1) {
            return sortAsc(sortedHeader);
        }
        return sortDesc(sortedHeader);


    }

    private List<ReservationDto> sortAsc(String sortedHeader) {
        switch (sortedHeader) {
            case "checkin" -> {
                return reservationRepository.orderByCheckinAsc().stream().map(mapper::toReservationDto).toList();
            }
            case "checkout" -> {
                return reservationRepository.orderByCheckoutAsc().stream().map(mapper::toReservationDto).toList();
            }
            case "guest" -> {
                return reservationRepository.orderByLastNameAsc().stream().map(mapper::toReservationDto).toList();
            }
            case "number" -> {
                return reservationRepository.orderByCamperPlaceNumberAsc().stream().map(mapper::toReservationDto).toList();
            }
            default -> {
                return findAllReservationsDto();
            }
        }
    }

    private List<ReservationDto> sortDesc(String sortedHeader) {
        switch (sortedHeader) {
            case "checkin" -> {
                return reservationRepository.orderByCheckinDesc().stream().map(mapper::toReservationDto).toList();
            }
            case "checkout" -> {
                return reservationRepository.orderByCheckoutDesc().stream().map(mapper::toReservationDto).toList();
            }
            case "guest" -> {
                return reservationRepository.orderByLastNameDesc().stream().map(mapper::toReservationDto).toList();
            }
            case "number" -> {
                return reservationRepository.orderByCamperPlaceNumberDesc().stream().map(mapper::toReservationDto).toList();
            }
            default -> {
                return findAllReservationsDto();
            }
        }
    }

    @Transactional
    public void updateReservation(int id, ReservationRequest request) {
        if (camperPlaceService.checkIsCamperPlaceOccupied(request.camperPlace(), request.checkin(), request.checkout(), id)) {
            throw new ClientInputException("Camper Place Is Already Occupied");
        }
        Reservation reservation = findReservationById(id);
        Optional.ofNullable(request.checkin()).ifPresent(reservation::setCheckin);
        Optional.ofNullable(request.checkout()).ifPresent(reservation::setCheckout);
        Optional.of(request.camperPlace()).ifPresent(reservation::setCamperPlace);
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

    private boolean isLocalDateInBetweenCheckinAndCheckout(LocalDate checkin, LocalDate checkout) {
        return (LocalDate.now().isEqual(checkin) || LocalDate.now().isAfter(checkin))
                && (LocalDate.now().isEqual(checkout) || LocalDate.now().isBefore(checkout));
    }

}

