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

    @Transactional
     public Reservation setReservation(Reservation reservation,CamperPlace camperPlace, LocalDate checkin,LocalDate checkout){
        reservation.setCamperPlace(camperPlace);
        reservation.setCheckin(checkin);
        reservation.setCheckout(checkout);
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

     public Reservation findReservationById(int reservationId){
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        return reservation;
     }
     public Reservation findByUser_id(int userId){
        Reservation reservation = reservationRepository.findByUser_Id(userId);
        return reservation;
     }
     public List<Reservation> findAllReservationsByCamperPlace(int camperPlaceId){
        List<Reservation> reservations = reservationRepository.findAllByCamperPlace_Id (camperPlaceId);
        return reservations;
     }

    @Transactional
    public void deleteReservation(Reservation reservation){
        reservationRepository.delete(reservation);
     }
    @Transactional
     public void deleteIfExpired(){
         List<Reservation> reservations = findAllReservations();
         for(Reservation reservation : reservations){
             if (reservation.getCheckout().isBefore(LocalDate.now()) || reservation.getCheckout().isEqual(LocalDate.now())) {
                 deleteReservation(reservation);
             }
         }
     }
     @Transactional
     public void updateReservation(int reservationId,LocalDate newCheckin, LocalDate newCheckout) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        reservation.setCheckout(newCheckin);
        reservation.setCheckout(newCheckout);
        reservationRepository.save(reservation);
     }


}

