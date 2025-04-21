package CPSF.com.demo.controller;

import CPSF.com.demo.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("getReservationCountMonthly/{camperPlaceIds}/{month}/{year}")
    public int[][] getMonthlyReservationCount(@PathVariable List<Integer> camperPlaceIds, @PathVariable int month, @PathVariable int year) {
        return statisticsService.getReservationCountForCamperPlace(camperPlaceIds, month, year);

    }

    @GetMapping("/geRevenueMonthly/{camperPlaceIds}/{month}/{year}")
    public double[][] getCamperPlaceMonthlyRevenue(@PathVariable List<Integer> camperPlaceIds, @PathVariable int month, @PathVariable int year) {
        return statisticsService.getRevenueForCamperPlace(camperPlaceIds, month, year);
    }


}
