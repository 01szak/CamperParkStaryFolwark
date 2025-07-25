package CPSF.com.demo.service;

import CPSF.com.demo.DTO.StatisticsDTO;

import java.util.List;

public interface StatisticsService {

    List<StatisticsDTO> getStatisticsDTOWithRevenue(int month, int year);

    List<StatisticsDTO> getStatisticsDTOWithReservationCount(int month, int year);

}
