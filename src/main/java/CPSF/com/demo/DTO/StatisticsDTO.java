package CPSF.com.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class StatisticsDTO {
	private String name;
	private Double value;
}
