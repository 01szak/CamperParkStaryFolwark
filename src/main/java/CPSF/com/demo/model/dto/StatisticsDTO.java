package CPSF.com.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class StatisticsDTO extends DTO {
	private String name;
	private Double value;
}
