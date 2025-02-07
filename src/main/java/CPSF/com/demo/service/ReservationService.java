package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
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

@Service
@RequiredArgsConstructor
public class ReservationService {

    ReservationRepository reservationRepository;
     CamperPlaceService camperPlaceService;
    UserService userService;
@Autowired
    public ReservationService(ReservationRepository reservationRepository, CamperPlaceService camperPlaceService, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.camperPlaceService = camperPlaceService;
        this.userService = userService;
    }

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

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
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

    public List<Reservation> findReservationByReservationStatus(ReservationStatus... args) {
        List<Reservation> allReservations = findAllReservations();
        List<Reservation> reservations = new ArrayList<>();
        for (Reservation reservation : allReservations) {
            if (Arrays.stream(args)
                    .anyMatch(status -> status == reservation.getReservationStatus())) {
                reservations.add(reservation);
            }
        }
        return reservations;
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
}

