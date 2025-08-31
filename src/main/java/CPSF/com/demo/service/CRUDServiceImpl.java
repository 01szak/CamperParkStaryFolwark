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
    public Page<D> findAll(Pageable pageable){
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
        repository.save(t);
    };

    protected void delete(T t){
        repository.delete(t);
    };

    protected List<T> findBy(String fieldName, String value)
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
