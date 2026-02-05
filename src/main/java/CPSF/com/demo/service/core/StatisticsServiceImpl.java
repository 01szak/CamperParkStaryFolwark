package CPSF.com.demo.service.core;

import CPSF.com.demo.model.dto.StatisticsDTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.model.entity.Statistics;
import CPSF.com.demo.service.util.ReservationCalculator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CamperPlaceService camperPlaceService;

    private ReservationCalculator reservationCalculator;

    public List<StatisticsDTO> getStatisticsDTOWithRevenue(int month, int year) {
        return getStatistics(month, year).stream().map(s ->
                new StatisticsDTO(s.getCamperPlace().getIndex(), s.getRevenue())
        )
        .collect(Collectors.toList());
    }

    public List<StatisticsDTO> getStatisticsDTOWithReservationCount(int month, int year) {
        return getStatistics(month, year).stream().map(s ->
                new StatisticsDTO(s.getCamperPlace().getIndex(), (double) s.getReservationCount())
        )
        .collect(Collectors.toList());
    }

    private List<Statistics> getStatistics(int month, int year) {
        return generateStatistics(month, year);
    }

    @VisibleForTesting
    public List<Statistics> generateStatistics(int month, int year) {
        List<Statistics> statistics = new ArrayList<>();
        List<CamperPlace> camperPlaces = camperPlaceService.findAll().toList();

        if(camperPlaces.isEmpty()) {
            throw new RuntimeException("Found zero camperPlaces !!");
        }

        for (CamperPlace cp : camperPlaces) {
            statistics.add(
                    createStatistic(
                            year,
                            month,
                            cp,
                            countRevenue(year, month, cp),
                            countReservationCount(year, month, cp)
                    )
            );
        }
        return statistics;
    }

    private long countReservationCount(int month, int year, CamperPlace camperPlace) {
        List<Reservation> reservations = getReservationsBasedOnDateWithNoComingStatus(month, year, camperPlace);
        return reservations.size();
    }


    private double countRevenue(int month, int year, CamperPlace camperPlace) {
        List<Reservation> paidReservations = getPaidReservationsBasedOnDate(month, year, camperPlace);

        if (paidReservations.isEmpty()) {
            return 0;
        } else  {
            return paidReservations.stream().mapToDouble(r -> r.getPrice().doubleValue()).sum();
        }
    }

    private Statistics createStatistic(int year, int month, CamperPlace camperPlace, Double revenue, Long reservationCount) {
        return Statistics.builder()
                        .year(year)
                        .month(month)
                        .camperPlace(camperPlace)
                        .revenue(revenue)
                        .reservationCount(reservationCount)
                        .build();
    }

    private List<Reservation> getPaidReservationsBasedOnDate(int month, int year, CamperPlace camperPlace) {
        List<Reservation> reservations;
        if (year == 0) {
            reservations = reservationService.findByCamperPlaceIdIfPaid(camperPlace.getId());
        } else if (month == 0) {
            reservations = reservationService.findByYearAndCamperPlaceIdIfPaid(year, camperPlace.getId());
        } else  {
            reservations = reservationService.findByMonthYearAndCamperPlaceIdIfPaid(month, year, camperPlace.getId());
        }
        return reservations;
    }

    private List<Reservation> getReservationsBasedOnDateWithNoComingStatus(int month, int year, CamperPlace camperPlace) {
        List<Reservation> reservations;
        if (year == 0) {
            reservations = reservationService.findByCamperPlaceIdIfStatusNotComing(camperPlace.getId());
        } else if (month == 0) {
            reservations = reservationService.findByYearAndCamperPlaceIdIfStatusNotComing(year, camperPlace.getId());
        } else  {
            reservations = reservationService.findByMonthYearAndCamperPlaceIdIfStatusNotComing(month, year, camperPlace.getId());
        }
        return reservations;
    }
}
