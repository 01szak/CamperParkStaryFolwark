package CPSF.com.demo.repository;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
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

                SELECT r FROM Reservation r WHERE CONCAT(r.user.firstName, ' ', r.user.lastName) LIKE %:fullName%
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
}
