package CPSF.com.demo.entity;

import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ReservationMetadata {
	private Set<String> reserved = new HashSet<>();
	private Set<String> checkin = new HashSet<>();
	private Set<String> checkout = new HashSet<>();

	public void addReserved(String date) {
		reserved.add(date);
	}

	public void addReserved(Collection<String> dates) {
		reserved.addAll(dates);
	}

	public void addCheckin(String date) {
		checkin.add(date);
	}

	public void addCheckin(Collection<String> dates) {
		checkin.addAll(dates);
	}

	public void addCheckout(String date) {
		checkout.add(date);
	}

	public void addCheckout(Collection<String> dates) {
		checkout.addAll(dates);
	}

}
