package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Repository.ReservationRepository;
import CPSF.com.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository theReservationRepository) {
        this.reservationRepository = theReservationRepository;
    }
     public Reservation findByCamperPlace(int CamperPlaceId){
        return reservationRepository.findReservationByCamperPlace_Id(CamperPlaceId);

     }

}
