package CPSF.com.demo.service;

import CPSF.com.demo.entity.DbObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface CRUDService <T extends DbObject> {

    void create(T t);

    @EntityGraph()
    Page<T> findAll(Pageable pageable);

    T findById(int id);

    void update(int id, T t);

    void delete(int id);
}
