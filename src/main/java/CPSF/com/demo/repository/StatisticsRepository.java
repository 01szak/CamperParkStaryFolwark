package CPSF.com.demo.repository;

import CPSF.com.demo.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics,Integer> {

	List<Statistics> findByCamperPlace_IdAndMonthAndYear(int camperPlaceId, int month, int year);
	List<Statistics> findByCamperPlace_IdInAndMonthAndYear(List<Integer> camperPlaceId, int month, int year);

}
