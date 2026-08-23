package CPSF.com.demo.service.core;

import CPSF.com.demo.model.constant.DiscountType;
import CPSF.com.demo.model.constant.Operation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public DiscountCondition(
            double discountValue,
            Number discountConditionValue,
            Operation operation,
            DiscountType discountType
    ) {
        if (!supportedOperations.contains(operation)) {
            throw new IllegalArgumentException("Unsupported operation in Discount Condition: " + operation);
        }

        this.discountValue = discountValue;
        this.discountConditionValue = discountConditionValue;
        this.operation = operation;
        this.discountType = discountType;
    }

    public BigDecimal doDiscount(BigDecimal valueToDiscount, Number conditionValue) {
        if (!isConditionMet(conditionValue)) {
            return valueToDiscount;
        }
        final var discountValueBD = BigDecimal.valueOf(discountValue);
        switch (discountType) {
            case SUBTRACT_VAL -> {
                return valueToDiscount.subtract(discountValueBD);
            }
            case SUBTRACT_PERCENT -> {
                final var divisor = BigDecimal.valueOf(100);

                var discountAmount = valueToDiscount
                        .multiply(discountValueBD)
                        .divide(divisor, valueToDiscount.scale(), RoundingMode.HALF_UP);

                return valueToDiscount.subtract(discountAmount);
            }
            default -> {
                return valueToDiscount;
            }
        }
    }

    private boolean isConditionMet(Number conditionValue) {
        switch (operation) {
            case EQUALS -> {
              return conditionValue.doubleValue() == discountConditionValue.doubleValue();
            }
            case NOT_EQUALS -> {
              return conditionValue.doubleValue() != discountConditionValue.doubleValue();
            }
            case GREATER_THEN -> {
                return conditionValue.doubleValue() > discountConditionValue.doubleValue();
            }
            case LESS_THEN -> {
                  return conditionValue.doubleValue() < discountConditionValue.doubleValue();
            }
            //should never happen
            default -> throw new IllegalArgumentException("Unsupported operation in Discount Condition");
        }
    }

}
