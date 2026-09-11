package CPSF.com.demo.service.core;

import CPSF.com.demo.model.constant.JoinOperator;
import CPSF.com.demo.model.constant.Operation;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record SearchCriteria(
        @Nullable String joinObject,
        @NotNull String key,
        @NotNull Operation operation,
        @NotNull String value,
        @Nullable String secondValue,
        @Nullable JoinOperator joinOperator
){
    public SearchCriteria(String key, Operation operation, String value) {
        this(null, key, operation, value, null, null);
    }
    public SearchCriteria(String joinObject, String key, Operation operation, String value) {
        this(joinObject, key, operation, value, null, null);
    }
    public SearchCriteria(String key, Operation operation, String value, String secondValue) {
        this(null, key, operation, value, secondValue, null);
    }
    public SearchCriteria(String key, Operation operation, String value, JoinOperator joinOperator) {
        this(null, key, operation, value, null, joinOperator);
    }
}
