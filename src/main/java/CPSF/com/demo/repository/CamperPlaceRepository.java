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
        WITH RECURSIVE ocp AS ( 
            SELECT checkin as d, checkout 
            FROM reservation AS r
            WHERE r.camper_place_id = :cpId AND r.checkin >= CURDATE()
                
            UNION ALL
                
            SELECT DATE_ADD(d, INTERVAL 1 DAY), checkout
            FROM ocp
            WHERE d <= checkout
        )    
        SELECT d 
        FROM ocp
        WHERE d >= CURDATE()
    """, nativeQuery = true)
    List<LocalDate> getOccupiedDates(@Param("cpId") Integer cpId);
}
