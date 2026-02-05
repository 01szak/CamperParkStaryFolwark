package CPSF.com.demo.model.dto;


import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class PaidReservationsDTO extends DTO {

	private Set<String> paidDates = new HashSet<>();

	public void addDates (String date) {
		this.paidDates.add(date);
	}

	public void addDates (Collection<String> dates) {
		this.paidDates.addAll(dates);
	}

}
