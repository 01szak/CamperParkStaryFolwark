package CPSF.com.demo.repository;

import CPSF.com.demo.entity.CamperPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CamperPlaceRepository extends JpaRepository<CamperPlace, Integer> {
    @Query("Select coalesce(max(c.number),0) From CamperPlace  c")
    int findMaxNumber();
    CamperPlace findCamperPlaceByNumber(int number);
}
