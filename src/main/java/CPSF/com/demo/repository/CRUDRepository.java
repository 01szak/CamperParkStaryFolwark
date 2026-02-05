package CPSF.com.demo.repository;

import CPSF.com.demo.model.entity.DbObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CRUDRepository <T extends DbObject> extends JpaRepository<T, Integer> {

}
