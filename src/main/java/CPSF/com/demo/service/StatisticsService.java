package CPSF.com.demo.service;

import CPSF.com.demo.controller.CamperPlaceController;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.Statistics;
import CPSF.com.demo.repository.ReservationRepository;
import CPSF.com.demo.repository.StatisticsRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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


    public int[] getReservationCountForChart(int month, int year, int... camperPlaceId) {
        int[] dataForChart = new int[camperPlaceId.length];
        for (int i = 0; i <= camperPlaceId.length - 1; i++) {
            dataForChart[i] = (reservationCount(camperPlaceId[i], month, year));

        }
        return dataForChart;
    }
//TODO poprawic wyswietlanie dla wykresow z uzyciem tabeli statistics
    public double[] getRevenueForChart(int month, int year, int... camperPlaceId) {
        double[] dataForChart = new double[camperPlaceId.length];
        for (int i = 0; i <= camperPlaceId.length - 1; i++) {
            dataForChart[i] = (revenueCount(camperPlaceId[i], month, year));

        }
        return dataForChart;
    }

    //    @Scheduled(cron = "0 0 0 1 * ?")
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
    public void update(Reservation reservation) {
        int camperPlaceId = reservation.getCamperPlace().getId();
        int month = reservation.getCheckin().getMonthValue();
        int year = reservation.getCheckin().getYear();
        Statistics statisticsInstance = statisticsRepository.findByCamperPlace_IdAndMonthAndYear(camperPlaceId, month, year);
        if (statisticsInstance == null) {
            newStatisticsInstance(reservation);
        } else {
            statisticsInstance.setRevenue(revenueCount(camperPlaceId,month,year));
            statisticsInstance.setReservationCount(reservationCount(camperPlaceId,month,year));
        }
    }

}
