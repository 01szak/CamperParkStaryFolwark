package CPSF.com.demo.controller;

import CPSF.com.demo.entity.DTO.StatisticsDTO;
import CPSF.com.demo.service.StatisticsService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/getRevenue/{month}/{year}/{camperPlaceIds}")
    public List<StatisticsDTO> getRevenue(@PathVariable int month, @PathVariable int year, @PathVariable int ...camperPlaceIds) {
        return statisticsService.getStatisticsDTOWithRevenue(month, year, camperPlaceIds);
    }

    @GetMapping("/getReservationCount/{month}/{year}/{camperPlaceIds}")
    public List<StatisticsDTO> getReservationCount(@PathVariable int month, @PathVariable int year, @PathVariable int ...camperPlaceIds) {
        return statisticsService.getStatisticsDTOWithReservationCount(month, year, camperPlaceIds);
    }

}
