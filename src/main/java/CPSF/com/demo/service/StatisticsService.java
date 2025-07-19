package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.DTO.StatisticsDTO;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.ReservationCalculator;
import CPSF.com.demo.entity.Statistics;
import CPSF.com.demo.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ReservationService reservationService;
    private final CamperPlaceService camperPlaceService;
    private final ReservationCalculator reservationCalculator;

    public List<StatisticsDTO> getStatisticsDTOWithRevenue(int month, int year, int ...camperPlaceIds) {
        return getStatistics(month, year, camperPlaceIds).stream().map(s ->
                new StatisticsDTO(s.getCamperPlace().getIndex(), s.getRevenue())
        )
        .collect(Collectors.toList());
    }

    public List<StatisticsDTO> getStatisticsDTOWithReservationCount(int month, int year, int ...camperPlaceIds) {
        return getStatistics(month, year, camperPlaceIds).stream().map(s ->
                new StatisticsDTO(s.getCamperPlace().getIndex(), (double) s.getReservationCount())
        )
        .collect(Collectors.toList());
    }

    private List<Statistics> getStatistics(int month, int year, int... camperPlaceIds) {
        List<Integer> camperPlaceIdList = Arrays.stream(camperPlaceIds).boxed().toList();
        return generateStatistics(camperPlaceIdList, month, year);
    }

    @VisibleForTesting
    public List<Statistics> generateStatistics(List<Integer> camperPlaceIdList, int month, int year) {

        List<Statistics> statistics = new ArrayList<>();

        List<CamperPlace> camperPlaces = camperPlaceService.findCamperPlacesByIds(camperPlaceIdList);

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
        List<Reservation> reservations = returnReservationsBasedOnDate(month, year, camperPlace);
        return reservations.size();
    }

    private double countRevenue(int month, int year, CamperPlace camperPlace) {
        List<Reservation> reservations = returnReservationsBasedOnDate(month, year, camperPlace);
        List<Reservation> paidReservations = reservations.stream()
                        .filter(Reservation::getPaid)
                        .toList();

        if (paidReservations.isEmpty()) {
            return 0;
        } else  {
            return paidReservations.stream().mapToDouble(r ->
                    reservationCalculator.calculateFinalReservationCost(
                            r.getCheckin(), r.getCheckout(), r.getCamperPlace().getPrice()
                    )
            ).sum();
        }
    }

    private Statistics createStatistic(int year, int month, CamperPlace camperPlace, Double revenue, Long reservationCount) {
        return Statistics.builder()
                        .year(year)
                        .month(month)
                        .camperPlace(camperPlace)
                        .revenue(revenue != null ? revenue : 0)
                        .reservationCount(reservationCount == null ? 0 : reservationCount)
                        .build();
    }

    private List<Reservation> returnReservationsBasedOnDate(int month, int year, CamperPlace camperPlace) {
        List<Reservation> reservations;
        if (year == 0) {
            reservations = reservationService.findByCamperPlaceId(camperPlace.getId());
        }
        else if (month == 0) {
            reservations = reservationService.findByYearAndCamperPlaceId(year, camperPlace.getId());
        } else  {
            reservations = reservationService.findByMonthYearAndCamperPlaceId(month, year, camperPlace.getId());
        }
        return reservations;
    }

}
