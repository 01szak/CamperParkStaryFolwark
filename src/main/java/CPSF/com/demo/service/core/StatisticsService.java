package CPSF.com.demo.service.core;

import CPSF.com.demo.model.dto.StatisticsDTO;

import java.util.List;

public interface StatisticsService {

    List<StatisticsDTO> getStatisticsDTOWithRevenue(int month, int year);

    List<StatisticsDTO> getStatisticsDTOWithReservationCount(int month, int year);

}
