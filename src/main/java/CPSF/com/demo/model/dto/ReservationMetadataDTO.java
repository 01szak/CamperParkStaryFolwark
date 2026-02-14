package CPSF.com.demo.model.dto;


import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationMetadataDTO extends DTO {
	private Set<String> reserved;
	private Set<String> checkin;
	private Set<String> checkout;
}
