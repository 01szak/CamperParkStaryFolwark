package CPSF.com.demo.service.core;

import CPSF.com.demo.model.constant.Operation;

public record SearchCriteria(
        String key,
        Operation operation,
        String value,
        String secondValue
){
    public SearchCriteria(String key, Operation operation, String value) {
        this(key, operation, value, null);
    }
}
