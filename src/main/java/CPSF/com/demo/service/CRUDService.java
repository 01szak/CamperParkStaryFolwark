package CPSF.com.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CRUDService<T, D> {

    Page<T> findAll(Pageable pageable);

    Page<T> findAll();

    Page<D> findAllDTO(Pageable pageable);

    Page<D> findAllDTO();

    T findById(int id);

    void update(T t);

    void delete(T t);

    void delete(int id);

    void create(T t);

    List<T> findBy(String fieldName, String value) throws InstantiationException, IllegalAccessException, NoSuchFieldException;

}
