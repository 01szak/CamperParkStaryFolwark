package CPSF.com.demo.service.implementation;

import CPSF.com.demo.DTO.DTO;
import CPSF.com.demo.entity.DbObject;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.service.CRUDService;
import CPSF.com.demo.util.Mapper;
import exception.ClientInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
        var sort = pageable.getSort();

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
    public Page<T> findBy(Pageable pageable, String fieldName, String value)
            throws InstantiationException, IllegalAccessException, NoSuchFieldException {
       //get instance of an object
        T t = getClassForDbObject().newInstance();
        Field field = getClassForDbObject().getDeclaredField(fieldName);

        ExampleMatcher matcher = getExampleMatcherWithIgnoreFields(field);

        //set the value of chosen field to the selected value
        field.setAccessible(true);

        if (field.getType().equals(LocalDate.class)) {
            LocalDate ldValue = LocalDate.parse(value);
            field.set(t, ldValue);
        } else if (field.getType().isEnum()) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
            value = value.toUpperCase();

            Object enumValue = Enum.valueOf(enumType, value);
            field.set(t, enumValue);
        } else if (field.getType().equals(Boolean.class)) {
            boolean condition;
            if (value.toLowerCase().equals("tak")) {
                condition = true;
            }else if (value.toLowerCase().equals("nie")) {
                condition = false;
            } else {
                throw new ClientInputException("Podano złą wartość");
            }
            field.set(t, condition);
        }
        else {
            field.set(t, value);
        }

        //find by this example
        Example<T> example = Example.of(t, matcher);

        return repository.findAll(example, pageable);
    }

    private  ExampleMatcher getExampleMatcherWithIgnoreFields(Field field) {
        Field[] allFields = getClassForDbObject().getDeclaredFields();
        Field[] superClassFields = getClassForDbObject().getSuperclass().getDeclaredFields();
        List<Field> ignoredFields = new ArrayList<>(Arrays.stream(allFields).toList());
        ignoredFields.addAll(Arrays.stream(superClassFields).toList());

        ignoredFields.removeIf(f -> f.equals(field));

        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths(ignoredFields.stream().map(Field::getName).toArray(String[]::new));
    }

    @Override
    public Page<D> findDTOBy(Pageable pageable, String fieldName, String value)
            throws NoSuchFieldException, InstantiationException, IllegalAccessException {

        return findBy(pageable, fieldName, value).map(entity -> (D) Mapper.toDTO(entity));
    }


    @SuppressWarnings("unchecked")
    private Class<T> getClassForDbObject() {
        return (Class<T>) ((ParameterizedType) super.getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }



}
