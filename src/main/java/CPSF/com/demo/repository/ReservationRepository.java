package CPSF.com.demo.repository;

import CPSF.com.demo.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {


    List<Reservation> findByUser_Id(int userId);
    List<Reservation> findAllByCamperPlace_Id (int camperPlaceId);
}
