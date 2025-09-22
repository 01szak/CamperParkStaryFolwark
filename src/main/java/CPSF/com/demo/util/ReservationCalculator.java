package CPSF.com.demo.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReservationCalculator {

	private static final long discount = 10;
	private static final long daysToDiscount = 4;


	public double calculateFinalReservationCost(long daysInrReservation, double camperPlacePrice) {
		long finalCost = 0;
		for (int i = 1; i <= daysInrReservation; i++ ) {
			if (i == daysToDiscount) {
				camperPlacePrice -= discount;
			}
			finalCost += (long) camperPlacePrice;

		}
		return finalCost;
	}

	public double calculateFinalReservationCost(LocalDate checkin, LocalDate checkout, double camperPlacePrice) {
		long numberOfDays = checkin.datesUntil(checkout).count();
		return calculateFinalReservationCost(numberOfDays, camperPlacePrice);
	}

}
