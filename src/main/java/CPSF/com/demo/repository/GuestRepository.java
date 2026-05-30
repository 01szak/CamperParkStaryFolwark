package CPSF.com.demo.repository;

import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.service.core.StatisticsService.StatisticsModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GuestRepository extends CRUDRepository<Guest> {

    @Query("""
        SELECT new CPSF.com.demo.service.core.StatisticsService$StatisticsModel$CountryDistribution (
            c, 
            cnt
        ) 
        FROM (
            SELECT g.country c, count(*) cnt 
                FROM Guest g JOIN Reservation r ON r.id = g.id 
                WHERE
                    (:month <= 0 OR FUNCTION('MONTH', r.checkin) = :month)
                AND
                    (:year <= 0 OR FUNCTION('YEAR', r.checkin) = :year)
                GROUP BY c
        )
            
    """)
    List<StatisticsModel.CountryDistribution> getCountryDistribution(@Param("month") int month, @Param("year") int year);
}

