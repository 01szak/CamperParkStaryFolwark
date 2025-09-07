package CPSF.com.demo.service;

import CPSF.com.demo.DTO.DTO;
import CPSF.com.demo.entity.DbObject;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;


@Service
public abstract class CRUDServiceImpl<T extends DbObject, D extends DTO> implements CRUDService<T, D> {

    private final CRUDRepository<T> repository;

    private final Sort sort = Sort.by(
            Sort.Direction.DESC,"updatedAt"
    );

    @Autowired
    protected CRUDServiceImpl(CRUDRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public void create(T t){
        repository.save(t);
    };

    @Override
    public Page<T> findAll(Pageable pageable){
        if (pageable.getSort().isEmpty()) {
            pageable =
                    PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return repository.findAll(pageable);
    }

    @Override
    @EntityGraph()
    public Page<T> findAll(){
        return findAll(Pageable.unpaged(sort));
    };

    @Override
    @EntityGraph()
    public Page<D> findAllDTO(Pageable pageable){
        return (Page<D>) findAll(pageable).map(Mapper::toDTO);
    }

    @Override
    @EntityGraph()
    public Page<D> findAllDTO(){
        return findAllDTO(Pageable.unpaged(sort));
    };

    @Override
    public T findById(int id){
        return repository.findById(id).orElseThrow();
    };

    @Override
    public void update(T t){
        repository.save(t);
    };

    @Override
    public void delete(T t){
        repository.delete(t);
    };

    @Override
    public void delete(int id){
        repository.deleteById(id);
    };

    @Override
    public List<T> findBy(String fieldName, String value)
            throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        T t = getClassForDbObject().newInstance();
        Field field = getClassForDbObject().getDeclaredField(fieldName);

        field.setAccessible(true);
        field.set(t, value);

        Example<T> example = Example.of(t);
        return repository.findAll(example);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getClassForDbObject() {
        return (Class<T>) ((ParameterizedType) super.getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }



}
