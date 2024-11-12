package CPSF.com.demo.Repository;

import CPSF.com.demo.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    public Reservation findReservationByCamperPlace_Id(int camperPlaceId);

    Reservation getReservationByUser_Id(int userId);
}
