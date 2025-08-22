package CPSF.com.demo.service;

import CPSF.com.demo.DTO.DTO;
import CPSF.com.demo.entity.DbObject;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public abstract class CRUDService <T extends DbObject, D extends DTO> {

    private final CRUDRepository<T> repository;

    private final Sort sort = Sort.by(
            Sort.Order.desc("updatedAt").nullsLast(),
            Sort.Order.desc("createdAt")
    );

    @Autowired
    protected CRUDService(CRUDRepository<T> repository) {
        this.repository = repository;
    }

    protected void create(T t){
        t.setCreatedAt(new Date());
        repository.save(t);
    };

    protected Page<T> findAll(Pageable pageable){
        if (pageable.getSort().isEmpty()) {
            pageable =
                    PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return repository.findAll(pageable);
    }

    @EntityGraph()
    protected Page<T> findAll(){
        return findAll(Pageable.unpaged(sort));
    };

    @EntityGraph()
    protected Page<D> findAllDTO(Pageable pageable){
        return (Page<D>) findAll(pageable).map(Mapper::toDTO);
    }

    @EntityGraph()
    protected Page<D> findAllDTO(){
        return findAllDTO(Pageable.unpaged(sort));
    };

    protected T findById(int id){
        return repository.findById(id).orElseThrow();
    };

    protected void update(T t){
        t.setUpdatedAt(new Date());
        repository.save(t);
    };

    protected void delete(T t){
        repository.delete(t);
    };


}
