package CPSF.com.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CRUDService<T> {

    Page<T> findAll(Pageable pageable);

    Page<T> findBy(Pageable pageable, String fieldName, String value) throws InstantiationException, IllegalAccessException, NoSuchFieldException;

    T findById(int id);

    void create(T t);

    void update(T t);

    void delete(int id);

}
