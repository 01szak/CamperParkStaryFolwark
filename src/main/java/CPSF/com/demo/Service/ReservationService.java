package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Repository.ReservationRepository;
import CPSF.com.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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

}
