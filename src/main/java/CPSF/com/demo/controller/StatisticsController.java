package CPSF.com.demo.controller;

import CPSF.com.demo.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/getMonthlyReservationCount/{camperPlaceId}/{month}/{year}")
    public int getMonthlyReservationCount(@PathVariable int camperPlaceId, @PathVariable int month, @PathVariable int year) {
        return statisticsService.reservationCount(camperPlaceId, month, year);
    }

    @GetMapping("/getCamperPlaceMonthlyRevenue/{camperPlaceId}/{month}/{year}")
    public double getCamperPlaceMonthlyRevenue(@PathVariable int camperPlaceId, @PathVariable int month, @PathVariable int year) {
        return statisticsService.revenueCount(camperPlaceId, month, year);
    }
}
