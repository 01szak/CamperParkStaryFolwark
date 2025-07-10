package CPSF.com.demo.entity.DTO;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.service.StatisticsService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class StatisticsDTO {
	private String name;
	private Double value;
}
