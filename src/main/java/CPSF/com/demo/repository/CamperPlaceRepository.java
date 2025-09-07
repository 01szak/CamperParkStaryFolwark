package CPSF.com.demo.repository;

import CPSF.com.demo.controller.CamperPlaceController;
import CPSF.com.demo.entity.CamperPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CamperPlaceRepository extends CRUDRepository<CamperPlace> {

    @EntityGraph(attributePaths = {"reservations", "reservations.user"})
     CamperPlace findByIndex(String index);

}
