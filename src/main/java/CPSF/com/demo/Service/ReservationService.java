package CPSF.com.demo.Service;

import CPSF.com.demo.Entity.CamperPlace;
import CPSF.com.demo.Entity.Reservation;
import CPSF.com.demo.Entity.User;
import CPSF.com.demo.Repository.ReservationRepository;
import CPSF.com.demo.Repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CamperPlaceService camperPlaceService;

    @Autowired
    public ReservationService(ReservationRepository theReservationRepository, CamperPlaceService camperPlaceService) {
        this.reservationRepository = theReservationRepository;
        this.camperPlaceService = camperPlaceService;
    }

    public Reservation findByCamperPlace(int CamperPlaceId){
        return reservationRepository.findReservationByCamperPlace_Id(CamperPlaceId);

     }
    @Transactional
     public Reservation setReservation(Reservation reservation,CamperPlace camperPlace, LocalDate enter,LocalDate checkout){
        reservation.setCamperPlace(camperPlace);
        reservation.setDateEnter(enter);
        reservation.setDateCheckout(checkout);
        reservationRepository.save(reservation);
        return reservation;
    }

    @Transactional
    public void createReservation(int camperPlaceNumber, LocalDate enter, LocalDate checkout){
        if (camperPlaceService.isCamperPlaceOccupied(camperPlaceNumber).equals(false)){

            CamperPlace camperPlace = camperPlaceService.findCamperPlaceById(camperPlaceNumber);
            setReservation(new Reservation(),camperPlace,enter,checkout);

            System.out.println(
                    "You have successfully made a reservation: \nid: "
                            + camperPlace.getId()
                            + "\ndate: " + enter + "/" + checkout);
        }else {

            System.out.println("The place you have chosen is occupied");

        }
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

    public Reservation getReservationByUserId(int userId){
        Reservation reservation = reservationRepository.getReservationByUser_Id(userId);
        return reservation;
    }

}

