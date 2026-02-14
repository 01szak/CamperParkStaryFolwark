package CPSF.com.demo.service.core;

import CPSF.com.demo.model.entity.DbObject;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.exception.ClientInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public abstract class CRUDServiceImpl<T extends DbObject> implements CRUDService<T> {

    @Autowired
    private CRUDRepository<T> repository;

    private final Sort UPDATED_AT_DESC = Sort.by(
            Sort.Direction.DESC,"updatedAt"
    );

    @Override
    public T create(T t){
        return repository.save(t);
    }

    @Override
    public Page<T> findAll(Pageable pageable){
        var sort = pageable.getSort();

        if (sort.isEmpty()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), UPDATED_AT_DESC);
        }
        return repository.findAll(pageable);
    }

    @Override
    @EntityGraph
    public Page<T> findAll(){
        return findAll(Pageable.unpaged(UPDATED_AT_DESC));
    }

    @Override
    public T findById(int id){
        return repository.findById(id).orElseThrow();
    }

    @Override
    public T update(T t){
        return repository.save(t);
    }

    @Override
    public List<T> update(List<T> t){
        return repository.saveAll(t);
    }

    @Override
    public void delete(int id){
        repository.deleteById(id);
    }

    public Page<T> findBy(String fieldName, String value) {
        return findBy(null, fieldName, value);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Page<T> findBy(Pageable pageable, String fieldName, String value) {
       if (pageable == null) {
           pageable = Pageable.unpaged(UPDATED_AT_DESC);
       }
        //get instance of an object
        try {
            T t = getClassForDbObject().newInstance();
            var field = getClassForDbObject().getDeclaredField(fieldName);
            var matcher = getExampleMatcherWithIgnoreFields(field);

            //set the value of chosen field to the selected value
            field.setAccessible(true);
            //         TODO: refactor this and handle exceptions
            if (field.getType().equals(LocalDate.class)) {
                var ldValue = LocalDate.parse(value);
                field.set(t, ldValue);
            } else if (field.getType().isEnum()) {
                value = value.toUpperCase();
                var enumType = (Class<? extends Enum>) field.getType();
                var enumValue = Enum.valueOf(enumType, value);
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
            var example = Example.of(t, matcher);

            return repository.findAll(example, pageable);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private  ExampleMatcher getExampleMatcherWithIgnoreFields(Field field) {
        Field[] allFields = getClassForDbObject().getDeclaredFields();
        Field[] superClassFields = getClassForDbObject().getSuperclass().getDeclaredFields();
        List<Field> ignoredFields = new ArrayList<>(Arrays.stream(allFields).toList());
        ignoredFields.addAll(Arrays.stream(superClassFields).toList());

        ignoredFields.removeIf(f -> f.equals(field));

        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths(ignoredFields.stream()
                        .map(Field::getName)
                        .toArray(String[]::new));
    }

    @SuppressWarnings("unchecked")
    private Class<T> getClassForDbObject() {
        return (Class<T>) ((ParameterizedType) super.getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

}
