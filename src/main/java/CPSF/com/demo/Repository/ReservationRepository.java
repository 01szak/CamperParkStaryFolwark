package CPSF.com.demo.Repository;

import CPSF.com.demo.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {

    Reservation findByUser_Id(int userId);

    List<Reservation> findAllByCamperPlace_Id (int camperPlaceId);
}
