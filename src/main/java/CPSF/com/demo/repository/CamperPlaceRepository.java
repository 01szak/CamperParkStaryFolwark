package CPSF.com.demo.repository;

import CPSF.com.demo.entity.CamperPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CamperPlaceRepository extends JpaRepository<CamperPlace, Integer> {

    CamperPlace findCamperPlaceByIndex(String index);

    List<CamperPlace> findCamperPlaceByIdIn(Collection<Integer> ids);
}
