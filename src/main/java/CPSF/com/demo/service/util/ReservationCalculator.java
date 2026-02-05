package CPSF.com.demo.service.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ReservationCalculator {

	private static final BigDecimal discount = BigDecimal.valueOf(10);
	private static final long daysToDiscount = 4;


	public BigDecimal calculateFinalReservationCost(long daysInrReservation, BigDecimal camperPlacePrice) {
		BigDecimal finalCost = BigDecimal.ZERO;
		for (int i = 1; i <= daysInrReservation; i++ ) {
			if (i == daysToDiscount) {
				camperPlacePrice = camperPlacePrice.subtract(discount);
			}
			finalCost = finalCost.add(camperPlacePrice);

		}
		return finalCost;
	}

	public BigDecimal calculateFinalReservationCost(LocalDate checkin, LocalDate checkout, BigDecimal camperPlacePrice) {
		long numberOfDays = checkin.datesUntil(checkout).count();
		return calculateFinalReservationCost(numberOfDays, camperPlacePrice);
	}

}
