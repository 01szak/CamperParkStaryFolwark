package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import CPSF.com.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CamperPlaceService camperPlaceService;

    @Autowired
    public ReservationService(ReservationRepository theReservationRepository, CamperPlaceService camperPlaceService) {
        this.reservationRepository = theReservationRepository;
        this.camperPlaceService = camperPlaceService;
    }

    @Transactional
    public void setReservation(Reservation reservation, CamperPlace camperPlace, LocalDate checkin, LocalDate checkout) {
        reservation.setCamperPlace(camperPlace);
        reservation.setCheckin(checkin);
        reservation.setCheckout(checkout);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void createReservation(int camperPlaceNumber, LocalDate enter, LocalDate checkout) {
        if (camperPlaceService.isCamperPlaceOccupied(camperPlaceNumber).equals(false)) {

            CamperPlace camperPlace = camperPlaceService.findCamperPlaceById(camperPlaceNumber);
             setReservation(new Reservation(), camperPlace, enter, checkout);

            System.out.println(
                    "You have successfully made a reservation: \nid: "
                            + camperPlace.getId()
                            + "\ndate: " + enter + "/" + checkout);
        } else {

            System.out.println("The place you have chosen is occupied");

        }
    }

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
    public List<Reservation>findReservationByReservationStatus(ReservationStatus ...args){
        List<Reservation> allReservations = reservationRepository.findAll();
        List<Reservation> reservations = new ArrayList<>();
        for(Reservation reservation : allReservations){
            if(Arrays.stream(args)
                    .anyMatch(status -> status == reservation.getReservationStatus())){
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


}

