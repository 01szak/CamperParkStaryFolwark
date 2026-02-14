package CPSF.com.demo.reservationCalculatorTest;

import CPSF.com.demo.service.util.ReservationCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ReservationCalculatorTest {

	private final ReservationCalculator reservationCalculator = new ReservationCalculator();

	@Test
	public void shouldReturnCorrectCostWithNoDiscount() {
		BigDecimal camperPlacePrice = BigDecimal.valueOf(100);
		BigDecimal expected = BigDecimal.valueOf(300);
		BigDecimal actual = reservationCalculator.calculateFinalReservationCost(3, camperPlacePrice);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnCorrectCostWithDiscount() {
		BigDecimal camperPlacePrice = BigDecimal.valueOf(100);
		BigDecimal expected = BigDecimal.valueOf(930);
		BigDecimal actual = reservationCalculator.calculateFinalReservationCost(10, camperPlacePrice);

		assertEquals(expected, actual);
	}
}
