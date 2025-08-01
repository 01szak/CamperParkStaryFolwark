package CPSF.com.demo.repository;

import CPSF.com.demo.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {

    @Query(
            "SELECT r FROM Reservation r WHERE FUNCTION('MONTH', r.checkin) = :month AND FUNCTION('YEAR', r.checkin) = :year AND r.camperPlace.id = :camperPlaceId"
    )
    List<Reservation> findByMonthYearAndCamperPlaceId(
            @Param("month") int month, @Param("year") int year, @Param("camperPlaceId") int camperPlaceId
    );

    @Query(
            "SELECT r FROM Reservation r WHERE FUNCTION('YEAR', r.checkin) = :year AND r.camperPlace.id = :camperPlaceId"
    )
    List<Reservation> findByYearAndCamperPlaceId(@Param("year") int year, @Param("camperPlaceId") int camperPlaceId);

    List<Reservation> findByCamperPlace_Id(int camperPlaceId);
    
    @Query("select reservation  from Reservation  reservation order by reservation.checkin desc ")
     List<Reservation> orderByCheckinDesc();

    @Query("select reservation  from Reservation  reservation order by reservation.checkout desc ")

     List<Reservation> orderByCheckoutDesc();
    @Query("select reservation  from Reservation  reservation order by reservation.user.lastName desc ")
     List<Reservation> orderByLastNameDesc();
    @Query("select reservation  from Reservation  reservation order by reservation.camperPlace.index desc ")
     List<Reservation> orderByCamperPlaceNumberDesc();

    @Query("select reservation  from Reservation  reservation order by reservation.checkin asc ")
     List<Reservation> orderByCheckinAsc();
    @Query("select reservation  from Reservation  reservation order by reservation.checkout asc ")

     List<Reservation> orderByCheckoutAsc();
    @Query("select reservation  from Reservation  reservation order by reservation.user.lastName asc ")
     List<Reservation> orderByLastNameAsc();
    @Query("select reservation  from Reservation  reservation order by reservation.camperPlace.index asc ")
     List<Reservation> orderByCamperPlaceNumberAsc();
    @Query(value = "select  reservation from Reservation reservation where reservation.camperPlace.id = ?1 and  month(reservation.checkin) = ?2 and  year(reservation.checkin) = ?3")
     List<Reservation> findReservationByCamperPlace_IdAndCheckin_MonthAndAndCheckin_Year(int id, int month, int year);
    @Query(value = "select  reservation from Reservation reservation where reservation.camperPlace.id = ?1  and  year(reservation.checkin) = ?2")

     List<Reservation> findReservationByCamperPlace_IdAndCheckin_Year(int id, int year);


}
