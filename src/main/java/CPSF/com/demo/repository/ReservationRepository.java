package CPSF.com.demo.repository;

import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.model.entity.Reservation.ReservationStatus;
import CPSF.com.demo.service.core.StatisticsService.StatisticsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends CRUDRepository<Reservation> {

        List<Reservation> findByReservationStatusNot(ReservationStatus status);


        Page<Reservation> findAllByCamperPlace_Index(Pageable pageable, String index);

        @Query("""

                SELECT r FROM Reservation r WHERE CONCAT(r.guest.firstname, ' ', r.guest.lastname) LIKE %:fullName%
       """)
        Page<Reservation> findAllByUserFullName(Pageable pageable ,@Param("fullName") String fullName);

        @Query("""
            SELECT r FROM Reservation r WHERE r.camperPlace.id = :camperPlaceId AND r.paid = true
        """)
        List<Reservation> findByCamperPlace_IdIfPaid(@Param("camperPlaceId") int camperPlaceId);

        @Query("""
            SELECT r FROM Reservation r WHERE FUNCTION('MONTH', r.checkin) = :month AND FUNCTION('YEAR', r.checkin) = :year AND r.camperPlace.id = :camperPlaceId AND r.paid = true
        """)
        List<Reservation> findAllByMonthYearAndCamperPlaceIdIfPaid(
                @Param("month") int month, @Param("year") int year, @Param("camperPlaceId") int camperPlaceId);

        @Query("""
                SELECT r FROM Reservation r WHERE FUNCTION('YEAR', r.checkin) = :year AND r.camperPlace.id = :camperPlaceId AND r.paid = true
        """)
        List<Reservation> findAllByYearAndCamperPlaceIdIfPaid(
                @Param("year") int year, @Param("camperPlaceId") int camperPlaceId);

        @Query("""
                SELECT r FROM Reservation r WHERE r.camperPlace.id = :camperPlaceId AND r.reservationStatus != 'COMING'
        """)
        List<Reservation> findAllByCamperPlaceIdIfStatusNotComing(@Param("camperPlaceId") int camperPlaceId);

        @Query("""
                SELECT r FROM Reservation r WHERE FUNCTION('YEAR', r.checkin) = :year AND r.camperPlace.id = :camperPlaceId AND r.reservationStatus != 'COMING'
        """)
        List<Reservation> findAllByYearAndCamperPlace_IdIfStatusNotComing(@Param("year") int year, @Param("camperPlaceId") int camperPlaceId);

        @Query("""
        SELECT r FROM Reservation r WHERE FUNCTION('YEAR', r.checkin) = :year AND FUNCTION('MONTH', r.checkin) = :month AND r.camperPlace.id = :camperPlaceId AND r.reservationStatus != 'COMING'
        """)
        List<Reservation> findAllByMonthYearAndCamperPlace_IdIfStatusNotComing(@Param("month") int month, @Param("year") int year, @Param("camperPlaceId") int camperPlaceId);

        @Query("""
            SELECT new CPSF.com.demo.service.core.StatisticsService$StatisticsModel$Revenue(
                cp.index, 
                count(r),
                COALESCE(SUM(r.price), 0)
            ) 
            FROM CamperPlace cp 
            LEFT JOIN cp.reservations r ON r.paid = :isPaid 
            AND (:month = 0 OR FUNCTION('MONTH', r.checkin) = :month) 
            AND (:year = 0 OR FUNCTION('YEAR', r.checkin) = :year)
            GROUP BY cp.index
        """)
        List<StatisticsModel.Revenue> countRevenueOfAllCamperPlaces(@Param("isPaid") boolean isPaid, @Param("month") int month, @Param("year") int year);

}
