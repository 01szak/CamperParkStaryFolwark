package CPSF.com.demo.controller;

import CPSF.com.demo.service.core.StatisticsService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/revenue/{month}/{year}")
    public List<List<StatisticsService.StatisticsModel.Revenue>> getRevenue(@PathVariable int month, @PathVariable int year) {
        return statisticsService.getRevenue(month, year);
    }
}
