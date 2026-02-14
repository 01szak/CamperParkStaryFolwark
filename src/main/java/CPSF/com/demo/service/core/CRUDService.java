package CPSF.com.demo.service.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CRUDService<T> {

    Page<T> findAll(Pageable pageable);

    Page<T> findAll();

    Page<T> findBy(Pageable pageable, String fieldName, String value)
            throws InstantiationException, IllegalAccessException, NoSuchFieldException;

    T findById(int id);

    T create(T t);

    T update(T t);

    List<T> update(List<T> t);

    void delete(int id);

}
