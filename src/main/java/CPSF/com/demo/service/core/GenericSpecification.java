package CPSF.com.demo.service.core;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GenericSpecification<S>(SearchCriteria searchCriteria) implements Specification<S> {

    @Override
    public @Nullable Predicate toPredicate(
            Root<S> root,
            CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder
    ) {
        if (searchCriteria.key() == null || searchCriteria.key().isEmpty()) return null;
        var key = (Expression) root.get(searchCriteria.key());
        var javaType = key.getJavaType();

        Object value = parseValue(javaType, searchCriteria.value());
        Object secondValue = searchCriteria.secondValue() != null ? parseValue(javaType, searchCriteria.secondValue()) : null;

        switch (searchCriteria.operation()) {
            case EQUALS -> {
                return criteriaBuilder.equal(key, value);
            }
            case NOT_EQUALS -> {
                return criteriaBuilder.notEqual(key, value);
            }
            case LIKE -> {
                return criteriaBuilder.like(criteriaBuilder.lower(key.as(String.class)), "%" + value.toString().toLowerCase() + "%");
            }
            case LESS_THEN -> {
                return criteriaBuilder.lessThan((Expression<? extends Comparable>) key, (Comparable) value);
            }
            case GREATER_THEN -> {
                return criteriaBuilder.greaterThan((Expression<? extends Comparable>) key, (Comparable) value);
            }
            case BETWEEN -> {
                return criteriaBuilder.between(
                        (Expression<? extends Comparable>) key,
                        (Comparable) value,
                        (Comparable) secondValue
                );
            }
            default -> {
                return null;
            }
        }
    }

    private Object parseValue(Class javaType, String value) {
        if (value == null) {
            return null;
        } else if (LocalDate.class.isAssignableFrom(javaType)) {
            return LocalDate.parse(value);
        } else if (BigDecimal.class.isAssignableFrom(javaType)) {
            return new BigDecimal(value);
        } else if (Integer.class.isAssignableFrom(javaType)||int.class.isAssignableFrom(javaType) ) {
            return Integer.parseInt(value);
        } else if (Boolean.class.isAssignableFrom(javaType)||boolean.class.isAssignableFrom(javaType)) {
            return Boolean.valueOf(value);
        } else if (Enum.class.isAssignableFrom(javaType)) {
            return Enum.valueOf((Class<? extends Enum>) javaType, value);
        } else  {
            return value;
        }
    }

}
