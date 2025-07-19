package CPSF.com.demo.ReservationCalculatorTest;

import CPSF.com.demo.util.ReservationCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ReservationCalculatorTest {

	private final ReservationCalculator reservationCalculator = new ReservationCalculator();

	@Test
	public void shouldReturnCorrectCostWithNoDiscount() {
		double camperPlacePrice = 100;
		double expected = 300;
		double actual = reservationCalculator.calculateFinalReservationCost(3, camperPlacePrice);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnCorrectCostWithDiscount() {
		double camperPlacePrice = 100;
		double expected = 930;
		double actual = reservationCalculator.calculateFinalReservationCost(10, camperPlacePrice);

		assertEquals(expected, actual);
	}
}
