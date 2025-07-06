package CPSF.com.demo.entity;

import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class PaidReservations {

	private Set<String> paidDates = new HashSet<>();;

	public void addDates (String date) {
		this.paidDates.add(date);
	}

	public void addDates (Collection<String> dates) {
		this.paidDates.addAll(dates);
	}

}
