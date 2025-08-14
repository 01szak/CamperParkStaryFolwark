package CPSF.com.demo.service;

import CPSF.com.demo.DTO.DTO;
import CPSF.com.demo.entity.DbObject;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

@Service
public abstract class CRUDService <T extends DbObject, D extends DTO> {

    private final CRUDRepository<T> repository;

    @Autowired
    protected CRUDService(CRUDRepository<T> repository) {
        this.repository = repository;
    }

    protected void create(T t){
        repository.save(t);
    };

    protected Page<T> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    @EntityGraph()
    protected Page<T> findAll(){
        Sort sort = Sort.by(
                Sort.Order.desc("updatedAt").nullsLast(),
                Sort.Order.desc("createdAt")
        );
        return findAll(Pageable.unpaged(sort));
    };

    @EntityGraph()
    protected Page<D> findAllDTO(Pageable pageable){
        return (Page<D>) findAll(pageable).map(Mapper::toDTO);
    }
    @EntityGraph()
    protected Page<D> findAllDTO( ){
        return (Page<D>) findAll().map(Mapper::toDTO);
    };

    protected T findById(int id){
        return repository.findById(id).orElseThrow();
    };

    protected void update(int id, T t){
        T object = findById(id);
        repository.save(object);
    };

    protected void delete(T t){
        repository.delete(t);
    };
}
