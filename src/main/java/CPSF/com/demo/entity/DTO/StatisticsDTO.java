package CPSF.com.demo.entity.DTO;

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
