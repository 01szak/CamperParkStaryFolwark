package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.User;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import CPSF.com.demo.repository.UserRepository;
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

    @Autowired
    private final ReservationRepository reservationRepository;
//    @Autowired
//    private final CamperPlaceService camperPlaceService;
//    @Autowired
//    private final UserService userService;
    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


//    @Transactional
//    public void setReservation(CamperPlace camperPlace, LocalDate checkin, LocalDate checkout) {
//        Reservation reservation = new Reservation();
//        User theUser = (User) userService.loadUserByUsername(authentication.getName());
//        reservation.setUser(theUser);
//        System.out.println(theUser);
//        reservation.setCamperPlace(camperPlace);
//        reservation.setCheckin(checkin);
//        reservation.setCheckout(checkout);
//        reservationRepository.save(reservation);
//    }

//    @Transactional
//    public void createReservation(int camperPlaceNumber, LocalDate checkin, LocalDate checkout) {
//
//        if (camperPlaceService.isCamperPlaceOccupied(camperPlaceNumber).equals(false)) {
//
//            CamperPlace camperPlace = camperPlaceService.findCamperPlaceById(camperPlaceNumber);
//            setReservation(camperPlace, checkin, checkout);
//
//            System.out.println(
//                    "You have successfully made a reservation: \nid: "
//                            + camperPlace.getId()
//                            + "\ndate: " + checkin + "/" + checkout);
//        } else {
//
//            System.out.println("The place you have chosen is occupied");
//
//        }
//    }

    public List<Reservation> findAllReservations() {
        List<Reservation> allReservations = reservationRepository.findAll();
        return allReservations;
    }

    public Reservation findReservationById(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        return reservation;
    }

    public List<Reservation> findByUserId(int userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        return reservations;
    }

    public List<Reservation> findAllReservationsByCamperPlace(int camperPlaceId) {
        List<Reservation> reservations = reservationRepository.findAllByCamperPlaceId(camperPlaceId);
        return reservations;
    }

    public List<Reservation> findReservationByReservationStatus(ReservationStatus... args) {
        List<Reservation> allReservations = reservationRepository.findAll();
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

