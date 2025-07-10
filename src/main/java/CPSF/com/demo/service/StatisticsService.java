package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.DTO.StatisticsDTO;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.Statistics;
import CPSF.com.demo.repository.StatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final ReservationService reservationService;


	@Transactional
    public void update(Reservation reservation, CamperPlace camperPlace) {

        int month = reservation.getCheckin().getMonthValue();
        int year = reservation.getCheckin().getYear();

        Set<Statistics> statisticsSet = statisticsRepository.findByCamperPlace_IdAndMonthAndYear(camperPlace.getId(), month, year);

        if (statisticsSet.isEmpty()) {
           createStatistic(
                   year, month, camperPlace, countRevenue(month, year, camperPlace), countReservationCount(month, year, camperPlace)
           );
        } else if (statisticsSet.size() == 1)  {
            Statistics  s = statisticsSet.iterator().next();
            s.setRevenue(countRevenue(month, year, camperPlace));
            s.setReservationCount(countReservationCount(month, year, camperPlace));
            statisticsRepository.save(s);
        } else {
            throw new RuntimeException("found this statistic instances: " + statisticsSet);
        }
    }
    public Set<Statistics> getStatistics(int month, int year, CamperPlace camperPlace) {
        return statisticsRepository.findByCamperPlace_IdAndMonthAndYear(camperPlace.getId(), month, year);
    }
    public List<StatisticsDTO> getStatisticsDTOWithRevenue(int month, int year, CamperPlace camperPlace) {
        return getStatistics(month, year, camperPlace).stream().map(s ->
                new StatisticsDTO(s.getCamperPlace().getIndex(), s.getRevenue())
        )
        .collect(Collectors.toList());
    }

    public List<StatisticsDTO> getStatisticsDTOWithReservationCount(int month, int year, CamperPlace camperPlace) {
        return getStatistics(month, year, camperPlace).stream().map(s ->
                new StatisticsDTO(s.getCamperPlace().getIndex(), (double) s.getReservationCount())
        )
        .collect(Collectors.toList());
    }

    private long countReservationCount(int month, int year, CamperPlace camperPlace) {
        return reservationService.findByMonthYearAndCamperPlaceId(month, year, camperPlace).size();
    }

    private double countRevenue(int month, int year, CamperPlace camperPlace) {
        List<Reservation> reservations = reservationService.findByMonthYearAndCamperPlaceId(month, year, camperPlace);

        if (reservations.isEmpty()) {
            return 0;
        } else  {
            return reservations.stream().mapToDouble(Reservation::calculateFinalPrice).sum();
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
