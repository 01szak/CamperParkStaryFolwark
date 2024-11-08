package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Repository.ReservationRepository;
import CPSF.com.demo.Repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository theReservationRepository) {
        this.reservationRepository = theReservationRepository;
    }
     public Reservation findByCamperPlace(int CamperPlaceId){
        return reservationRepository.findReservationByCamperPlace_Id(CamperPlaceId);

     }
        @Transactional
     public Reservation setReservation(Reservation reservation,CamperPlace camperPlace, LocalDate enter,LocalDate checkout){
        reservation.setCamperPlace(camperPlace);
        reservation.setDateEnter(enter);
        reservation.setDateCheckout(checkout);
        camperPlace.setIsOccupied(1);
        reservationRepository.save(reservation);
        return reservation;
     }

     public List<Reservation> findAllReservations(){
        List<Reservation> allReservations = reservationRepository.findAll();
        return allReservations;
     }
     public void deleteReservation(Reservation reservation){
        reservationRepository.delete(reservation);
     }

     public void deleteIfExpired(){
         List<Reservation> reservations = findAllReservations();
         for(Reservation reservation : reservations){
             if (reservation.getDateCheckout().isBefore(LocalDate.now()) || reservation.getDateCheckout().isEqual(LocalDate.now())) {
                 deleteReservation(reservation);
             }
         }
     }

}

