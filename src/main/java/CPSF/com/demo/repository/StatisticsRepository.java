package CPSF.com.demo.repository;

import CPSF.com.demo.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics,Integer> {
    Statistics findByCamperPlace_IdAndMonthAndYear(int camperPlaceId, int month, int year);

    List<Statistics> findByCamperPlace_IdAndYear(int camperPlaceId, int year);
}
