package CPSF.com.demo;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class TestUtil {


	public static List<Reservation> reservationGenerator(int count, boolean isPaid, CamperPlace camperPlace, int month, int year) {

		List<Reservation> generatedReservations = new ArrayList<>();
		List<Integer> occupiedDays = new ArrayList<>();

		camperPlace.getReservations().forEach(r -> {
			occupiedDays.addAll(
					r.getCheckin().plusDays(1).datesUntil(r.getCheckout())
							.map(LocalDate::getDayOfMonth)
							.toList()
			);
		});

		Random random = new Random();
		for (int i = 0; i < count; i++) {
			int r, r2;
			int attempts = 0;
			do {
				r = random.nextInt(1, 31);
				r2 = random.nextInt(1, 31);
				attempts++;
			} while ((r >= r2 || containsAnyDayBetween(occupiedDays, r, r2)) && attempts < 100);


			if (attempts >= 100) {
				throw new IllegalStateException("Free dates could not be found in given tries");
			}

			LocalDate randomCheckin = LocalDate.of(year, month, r);
			LocalDate randomCheckout = LocalDate.of(year, month, r2);

			for (int day = r + 1; day < r2; day++) {
				occupiedDays.add(day);
			}

			generatedReservations.add(
					Reservation.builder()
							.camperPlace(camperPlace)
							.checkin(randomCheckin)
							.checkout(randomCheckout)
							.paid(isPaid)
							.build()

			);
		}
		return generatedReservations;
	}

	private static boolean containsAnyDayBetween(List<Integer> occupiedDays, int start, int end) {
		for (int day = start + 1; day < end; day++) {
			if (occupiedDays.contains(day)) {
				return true;
			}
		}
		return false;
	}

}
