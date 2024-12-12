package CPSF.com.demo.repository;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {


    List<Reservation> findByUserId(int userId);
    List<Reservation> findAllByCamperPlaceId (int camperPlaceId);
//
//   @Query("SELECT Reservation r from Reservation r where r.reservationStatus = ?)

}
