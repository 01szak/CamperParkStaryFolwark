package CPSF.com.demo.Repository;

import CPSF.com.demo.Entity.CamperPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface CamperPlaceRepository extends JpaRepository<CamperPlace,Integer> {

    public CamperPlace findCamperPlaceById(int id);
}
