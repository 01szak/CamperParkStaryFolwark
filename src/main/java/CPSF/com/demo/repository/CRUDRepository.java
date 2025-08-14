package CPSF.com.demo.repository;

import CPSF.com.demo.entity.DbObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CRUDRepository <T extends DbObject> extends JpaRepository<T, Integer> {
}
