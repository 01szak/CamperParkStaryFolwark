package CPSF.com.demo.service;

import CPSF.com.demo.DTO.DTO;
import CPSF.com.demo.entity.DbObject;
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

    List<T> findBy(String fieldName, String value);

}
