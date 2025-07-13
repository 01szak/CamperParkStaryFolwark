package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.DTO.StatisticsDTO;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.ReservationCalculator;
import CPSF.com.demo.entity.Statistics;
import CPSF.com.demo.repository.StatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final ReservationService reservationService;
    private final CamperPlaceService camperPlaceService;
    private final ReservationCalculator reservationCalculator;


    @Transactional
    public void update(Reservation reservation, CamperPlace camperPlace) {

        int month = reservation.getCheckin().getMonthValue();
        int year = reservation.getCheckin().getYear();

        List<Statistics> statistics =
                statisticsRepository.findByCamperPlace_IdAndMonthAndYear(camperPlace.getId(), month, year);

        if (statistics.isEmpty()) {
           createStatistic(
                   year,
                   month,
                   camperPlace,
                   countRevenue(month, year, camperPlace),
                   countReservationCount(month, year, camperPlace)
           );
        } else if (statistics.size() == 1)  {
            Statistics  s = statistics.get(0);
            s.setRevenue(
                    reservation.getPaid() ? countRevenue(month, year, camperPlace) : 0
            );
            s.setReservationCount(countReservationCount(month, year, camperPlace));
            statisticsRepository.save(s);
        } else {
            throw new RuntimeException("found this statistic instances: " + statistics);
        }
    }

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

    private List<Statistics> getStatistics(int month, int year, int ...camperPlaceIds) {
        List<Statistics> statistics = new ArrayList<>();
        for (int i : camperPlaceIds) {
            List<Statistics> s = statisticsRepository.findByCamperPlace_IdAndMonthAndYear(i, month, year);
            statistics.add(
                   s.isEmpty() ? Statistics.builder()
                            .year(year)
                            .month(month)
                            .camperPlace(camperPlaceService.findCamperPlaceById(i))
                            .revenue(0)
                            .reservationCount(0)
                            .build()
                    : s.get(0)
            );
        }
        return statistics;
    }

    private long countReservationCount(int month, int year, CamperPlace camperPlace) {
        return reservationService.findByMonthYearAndCamperPlaceId(month, year, camperPlace).size();
    }

    private double countRevenue(int month, int year, CamperPlace camperPlace) {
        List<Reservation> reservations =
                reservationService.findByMonthYearAndCamperPlaceId(month, year, camperPlace);

        if (reservations.isEmpty()) {
            return 0;
        } else  {
            return reservations.stream().mapToDouble(r ->
                    reservationCalculator.calculateFinalReservationCost(r.getCheckin(), r.getCheckout(), r.getCamperPlace().getPrice())
            ).sum();
        }
    }

    private void createStatistic(int year, int month, CamperPlace camperPlace, Double revenue, Long reservationCount) {
        statisticsRepository.save(
                Statistics.builder()
                        .year(year)
                        .month(month)
                        .camperPlace(camperPlace)
                        .revenue(revenue != null ? revenue : 0)
                        .reservationCount(reservationCount != null ? reservationCount : 0)
                        .build()
        );
    }


}
