package CPSF.com.demo.service.core;

import CPSF.com.demo.model.constant.DiscountType;
import CPSF.com.demo.model.constant.Operation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReservationCalculatorService {


	public BigDecimal calculate(BigDecimal cpPrice, long daysInReservation) {
		//TODO this should be stored in DB
		final var condition = new DiscountCondition(10, 3, Operation.GREATER_THEN, DiscountType.SUBTRACT_VAL);
		var finalPrice = BigDecimal.ZERO;

		for (int i = 1; i <= daysInReservation; i++) {
			var discountedPrice = condition.doDiscount(cpPrice, i);
			if (discountedPrice instanceof Double p) {
				finalPrice = finalPrice.add(BigDecimal.valueOf(p));
			} else {
				finalPrice = finalPrice.add((BigDecimal) discountedPrice);
			}
		}

		return finalPrice;
	}

}
