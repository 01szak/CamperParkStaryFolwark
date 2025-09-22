package CPSF.com.demo.repository;

import CPSF.com.demo.DTO.ReservationDTO;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends CRUDRepository<Reservation> {

        @Query("SELECT r FROM Reservation r WHERE FUNCTION('MONTH', r.checkin) = :month AND FUNCTION('YEAR', r.checkin) = :year AND r.camperPlace.id = :camperPlaceId")
        List<Reservation> findByMonthYearAndCamperPlaceId(
                @Param("month") int month, @Param("year") int year, @Param("camperPlaceId") int camperPlaceId);

        List<Reservation> findByReservationStatusNot(ReservationStatus status);

        @Query("SELECT r FROM Reservation r WHERE FUNCTION('YEAR', r.checkin) = :year AND r.camperPlace.id = :camperPlaceId")
        List<Reservation> findByYearAndCamperPlaceId(@Param("year") int year, @Param("camperPlaceId") int camperPlaceId);

        List<Reservation> findByCamperPlace_Id(int camperPlaceId);

        Page<Reservation> findAllByCamperPlace_Index(Pageable pageable, String index);

        @Query("""
       SELECT r 
       FROM Reservation r 
       WHERE CONCAT(r.user.firstName, ' ', r.user.lastName) 
             LIKE %:fullName%
       """)
        Page<Reservation> findAllByUserFullName(Pageable pageable,@Param("fullName") String fullName);
}
