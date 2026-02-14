package CPSF.com.demo.repository;

import CPSF.com.demo.model.entity.CamperPlace;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface CamperPlaceRepository extends CRUDRepository<CamperPlace> {

}
