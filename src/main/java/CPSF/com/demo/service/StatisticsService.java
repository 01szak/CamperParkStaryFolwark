package CPSF.com.demo.service;

import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.Statistics;
import CPSF.com.demo.repository.ReservationRepository;
import CPSF.com.demo.repository.StatisticsRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class StatisticsService {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    StatisticsRepository statisticsRepository;
    @Autowired
    CamperPlaceService camperPlaceService;

    public int reservationCount(int camperPlaceId, int month, int year) {
        if (month == 0) {

            return reservationRepository.findReservationByCamperPlace_IdAndCheckin_Year(camperPlaceId, year).size();


        }
        return reservationRepository.findReservationByCamperPlace_IdAndCheckin_MonthAndAndCheckin_Year(camperPlaceId, month, year).size();
    }

    public double revenueCount(int camperPlaceId, int month, int year) {
        if (month == 0) {

            List<Reservation> reservations = reservationRepository.findReservationByCamperPlace_IdAndCheckin_Year(camperPlaceId, year);

            return reservations.stream().mapToDouble(Reservation::calculateFinalPrice).sum();
        }
        List<Reservation> reservations = reservationRepository.findReservationByCamperPlace_IdAndCheckin_MonthAndAndCheckin_Year(camperPlaceId, month, year);
        return reservations.stream().mapToDouble(Reservation::calculateFinalPrice).sum();
    }


    public int[][] getReservationCountForCamperPlace(List<Integer> ids, int month, int year) {
           int[][] chart = new int[ids.size()][2];
        try {
            if (month == 0) {
                for (int i = 0; i < ids.size() ; i++) {
                    chart[i][0] = ids.get(i);
                    chart[i][1] = statisticsRepository.findByCamperPlace_IdAndYear(ids.get(i), year).stream().mapToInt(Statistics::getReservationCount).sum();
                }
                return chart;
            } else {
                for (int i = 0; i < ids.size() ; i++) {
                    var s = statisticsRepository.findByCamperPlace_IdAndMonthAndYear(ids.get(i), month, year);
                    if(s == null) {
                        chart[i][0] = ids.get(i);
                        chart[i][1] = 0;
                    }else {
                        chart[i][0] = ids.get(i);
                        chart[i][1] = s.getReservationCount();
                    }
                }
                return chart;
            }
        } catch (NullPointerException e) {
            return chart;
        }

    }


public double[][] getRevenueForCamperPlace(List<Integer> ids, int month, int year) {
    double[][] chart = new double[ids.size()][2];
    try {
        if (month == 0) {
            for (int i = 0; i < ids.size() ; i++) {
                chart[i][0] = ids.get(i);
                chart[i][1] = statisticsRepository.findByCamperPlace_IdAndYear(ids.get(i), year).stream().mapToDouble(Statistics::getRevenue).sum();
            }
            return chart;
        } else {
            for (int i = 0; i < ids.size() ; i++) {
                var s = statisticsRepository.findByCamperPlace_IdAndMonthAndYear(ids.get(i), month, year);
                if(s == null) {
                    chart[i][0] = ids.get(i);
                    chart[i][1] = 0;
                } else {
                    chart[i][0] = ids.get(i);
                    chart[i][1] = s.getRevenue();
                }
            }
            return chart;
        }
    } catch (NullPointerException e) {
        return chart;
    }

}

@Transactional
protected void newStatisticsInstance(Reservation reservation) {
    statisticsRepository.save(
            Statistics.builder()
                    .camperPlace(reservation.getCamperPlace())
                    .reservationCount(1)
                    .revenue(reservation.calculateFinalPrice())
                    .month(reservation.getCheckin().getMonthValue())
                    .year(reservation.getCheckin().getYear())
                    .build()
    );
}

@Transactional
protected void newStatisticsInstance(CamperPlace camperPlace, int month, int year) {
    statisticsRepository.save(
            Statistics.builder()
                    .camperPlace(camperPlace)
                    .reservationCount(reservationCount(camperPlace.getId(), month, year))
                    .revenue(revenueCount(camperPlace.getId(), month, year))
                    .month(month)
                    .year(year)
                    .build()
    );
 }


@Transactional
public void update(Reservation reservation) {
        int camperPlaceId = reservation.getCamperPlace().getId();
        int month = reservation.getCheckin().getMonthValue();
        int year = reservation.getCheckin().getYear();
        Statistics statisticsInstance = statisticsRepository.findByCamperPlace_IdAndMonthAndYear(camperPlaceId, month, year);
        if (statisticsInstance == null) {
            newStatisticsInstance(reservation);
        } else {
            statisticsInstance.setRevenue(revenueCount(camperPlaceId, month, year));
            statisticsInstance.setReservationCount(reservationCount(camperPlaceId, month, year));
        }

}


}
