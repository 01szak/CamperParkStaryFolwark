package CPSF.com.demo.service.core;

public record SearchCriteria(
        String key,
        Operation operation,
        String value,
        String secondValue
){
    public SearchCriteria(String key, Operation operation, String value) {
        this(key, operation, value, null);
    }

    public enum Operation {
        EQUALS,
        NOT_EQUALS,
        LESS_THEN,
        GREATER_THEN,
        BETWEEN,
        LIKE
    }
}
