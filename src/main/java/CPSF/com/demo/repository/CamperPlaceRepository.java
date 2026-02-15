package CPSF.com.demo.repository;

import CPSF.com.demo.model.entity.CamperPlace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamperPlaceRepository extends CRUDRepository<CamperPlace> {

    @Query("""
        select c from CamperPlace c order by cast(c.index as integer ), c.index
    """)
    List<CamperPlace> findAllOrderByIndex();


}
