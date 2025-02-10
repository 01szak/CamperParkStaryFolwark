package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.DTO.ReservationDto;
import CPSF.com.demo.entity.Mapper;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserService userService;
    @Autowired
    Mapper mapper;

    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    @Transactional
    public void createReservation(LocalDate checkin, LocalDate checkout, CamperPlace camperPlace, User user) {
        userService.create(user);

        System.out.println(
                "You have successfully made a reservation: \nid: "
                        + camperPlace.getId()
                        + "\ndate: " + checkin + "/" + checkout);

        reservationRepository.save(Reservation.builder()
                .checkin(checkin)
                .checkout(checkout)
                .camperPlace(camperPlace)
                .user(user)
                .build());

    }

    public List<ReservationDto> findAllReservations() {
        return reservationRepository.findAll().stream().map(mapper::toReservationDto).toList();
    }

    public Reservation findReservationById(int reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow();
    }

    public List<Reservation> findByUserId(int userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> findAllReservationsByCamperPlace(int camperPlaceId) {
        return reservationRepository.findAllByCamperPlaceId(camperPlaceId);
    }


    @Transactional
    public void deleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }


    @Transactional
    public void updateReservation(int reservationId, LocalDate newCheckin, LocalDate newCheckout) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        reservation.setCheckin(newCheckin);
        reservation.setCheckout(newCheckout);
        reservationRepository.save(reservation);
    }


    public List<Reservation> findAllUserReservations(int id) {
        return reservationRepository.findByUserId(id);
    }

    public List<ReservationDto> getFilteredData(String value) {

        List<ReservationDto> allReservationsDto = findAllReservations();
        if (value.equals("empty") || value.isEmpty() || value.equals(" ")) {
            return allReservationsDto;
        }

        List<ReservationDto> filteredList = new ArrayList<>();
        String filterValue = value.toLowerCase();
        allReservationsDto.forEach(reservationDto -> {
            if (reservationDto.getCheckin().toString().contains(filterValue) ||
                    reservationDto.getCheckout().toString().contains(filterValue) ||
                    reservationDto.getUserFirstName().toLowerCase().contains(filterValue) ||
                    reservationDto.getUserLastName().toLowerCase().contains(filterValue) ||
                    reservationDto.getReservationStatus().toLowerCase().contains(filterValue) ||
                    (isNumber(value) && reservationDto.getCamperPlaceNumber() == Integer.parseInt(value))

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
}

