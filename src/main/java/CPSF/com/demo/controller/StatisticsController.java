package CPSF.com.demo.controller;

import CPSF.com.demo.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/getMonthlyReservationCount/{camperPlaceId}/{month}/{year}")
    public int getMonthlyReservationCount(@PathVariable int camperPlaceId, @PathVariable int month, @PathVariable int year) {
        return statisticsService.getReservationCountForCamperPlace(camperPlaceId, month, year);
    }

    @GetMapping("/getCamperPlaceMonthlyRevenue/{camperPlaceId}/{month}/{year}")
    public double getCamperPlaceMonthlyRevenue(@PathVariable int camperPlaceId, @PathVariable int month, @PathVariable int year) {
        return statisticsService.getRevenueForCamperPlace(camperPlaceId, month, year);
    }

    @GetMapping("/getReservationCountForChart/{month}/{year}/{camperPlaceId}")
    public int[] getReservationCountForChart(@PathVariable int month, @PathVariable int year, @PathVariable int...camperPlaceId) {
        return statisticsService.getReservationCountForChart(month,year,camperPlaceId);
    }
    @GetMapping("/getRevenueForChart/{month}/{year}/{camperPlaceId}")
    public double[] getRevenueForChart(@PathVariable int month, @PathVariable int year, @PathVariable int...camperPlaceId) {
        return statisticsService.getRevenueForChart(month,year,camperPlaceId);
    }


}
