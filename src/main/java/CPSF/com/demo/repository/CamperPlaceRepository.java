package CPSF.com.demo.repository;

import CPSF.com.demo.model.entity.CamperPlace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CamperPlaceRepository extends CRUDRepository<CamperPlace> {

    @Query("""
        select c from CamperPlace c order by cast(c.index as integer ), c.index
    """)
    List<CamperPlace> findAllOrderByIndex();

    @Query("""
        select max(cast(c.index as integer)) i from CamperPlace c
    """)
    String getCamperplaceMaxIndex();

    List<CamperPlace> findCamperPlaceByPriceNotNullAndCamperPlaceType_Id(Integer cptId);

    @Query(value = """
        WITH RECURSIVE exc AS (
            SELECT DATE_ADD(checkin, INTERVAL 1 DAY ) as exc_date, checkout as exc_checkout 
            FROM reservation AS r
            WHERE r.id = :reservationId
                
            UNION ALL
            
            SELECT DATE_ADD(exc_date, INTERVAL 1 DAY), exc_checkout
            FROM exc
            WHERE DATE_ADD(exc_date, INTERVAL 1 DAY) < exc_checkout
        ),
        ocp AS ( 
            SELECT DATE_ADD(checkin, INTERVAL 1 DAY) as ocp_date, checkout as ocp_checkout 
            FROM reservation AS r
            WHERE r.camper_place_id = :cpId AND r.checkin >= CURDATE()
                
            UNION ALL
                
            SELECT DATE_ADD(ocp_date, INTERVAL 1 DAY), ocp_checkout
            FROM ocp
            WHERE DATE_ADD(ocp_date, INTERVAL 1 DAY) < ocp_checkout
        )
            
        SELECT DISTINCT ocp_date
        FROM ocp
        WHERE ocp_date >= CURDATE() AND ocp_date NOT IN (SELECT exc_date FROM exc)
    """, nativeQuery = true)
    List<LocalDate> getOccupiedDates(@Param("cpId") Integer cpId, @Param("reservationId") Integer reservationId);


}
