package CPSF.com.demo.repository;

import CPSF.com.demo.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {


    List<Reservation> findByUserId(int userId);
    List<Reservation> findAllByCamperPlaceId (int camperPlaceId);

    @Query("select reservation  from Reservation  reservation order by reservation.checkin desc ")
    public List<Reservation> orderByCheckinDesc();
    @Query("select reservation  from Reservation  reservation order by reservation.checkin desc ")
    public List<Reservation> orderByCheckoutDesc();
    @Query("select reservation  from Reservation  reservation order by reservation.user.lastName desc ")
    public List<Reservation> orderByLastNameDesc();
    @Query("select reservation  from Reservation  reservation order by reservation.camperPlace.number desc ")
    public List<Reservation> orderByCamperPlaceNumberDesc();
    @Query("select reservation  from Reservation  reservation order by reservation.checkin asc ")
    public List<Reservation> orderByCheckinAsc();
    @Query("select reservation  from Reservation  reservation order by reservation.checkin asc ")
    public List<Reservation> orderByCheckoutAsc();
    @Query("select reservation  from Reservation  reservation order by reservation.user.lastName asc ")
    public List<Reservation> orderByLastNameAsc();
    @Query("select reservation  from Reservation  reservation order by reservation.camperPlace.number asc ")
    public List<Reservation> orderByCamperPlaceNumberAsc();


}
