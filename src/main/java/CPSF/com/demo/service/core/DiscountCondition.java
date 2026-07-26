package CPSF.com.demo.service.core;

import CPSF.com.demo.model.constant.DiscountType;
import CPSF.com.demo.model.constant.Operation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static CPSF.com.demo.model.constant.Operation.EQUALS;
import static CPSF.com.demo.model.constant.Operation.GREATER_THEN;
import static CPSF.com.demo.model.constant.Operation.LESS_THEN;
import static CPSF.com.demo.model.constant.Operation.NOT_EQUALS;

@Getter
@Setter
public class DiscountCondition {

    private static final List<Operation> supportedOperations = List.of(EQUALS, NOT_EQUALS, GREATER_THEN, LESS_THEN);


    private double discountValue;
    private Number discountConditionValue;
    private Operation operation;
    private DiscountType discountType;

    public DiscountCondition(double discountValue, Number discountConditionValue, Operation operation, DiscountType discountType) {
        if (!supportedOperations.contains(operation)) {
            throw new IllegalArgumentException("Unsupported operation in Discount Condition: " + operation);
        }

        this.discountValue = discountValue;
        this.discountConditionValue = discountConditionValue;
        this.operation = operation;
        this.discountType = discountType;
    }

    public Object doDiscount(Object valueToDiscount, Object conditionValue) {
        if (!isConditionMet(conditionValue)) {
            return valueToDiscount;
        }

        var doubleValue = ((Number) valueToDiscount).doubleValue();

        switch (discountType) {
            case SUBTRACT_VAL -> {
                return doubleValue - discountValue;
            }
            case SUBTRACT_PERCENT -> {
                return doubleValue - doubleValue * (discountValue / 100);
            }
            default -> {
                return valueToDiscount;
            }
        }
    }

    private boolean isConditionMet(Object conditionValue) {
        if (!(conditionValue instanceof Number val)) {
            return false;
        }

        switch (operation) {
            case EQUALS -> {
              return val.doubleValue() == discountConditionValue.doubleValue();
            }
            case NOT_EQUALS -> {
              return val.doubleValue() != discountConditionValue.doubleValue();
            }
            case GREATER_THEN -> {
                return val.doubleValue() > discountConditionValue.doubleValue();
            }
            case LESS_THEN -> {
                  return val.doubleValue() < discountConditionValue.doubleValue();
            }
            //should never happen
            default -> throw new IllegalArgumentException("Unsupported operation in Discount Condition");
        }
    }

}
