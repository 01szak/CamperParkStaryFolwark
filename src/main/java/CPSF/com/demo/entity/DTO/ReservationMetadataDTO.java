package CPSF.com.demo.entity.DTO;

import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationMetadataDTO {
	private Set<String> reserved;
	private Set<String> checkin;
	private Set<String> checkout;
}
