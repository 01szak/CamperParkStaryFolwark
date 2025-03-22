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


    public int[] getReservationCountForChart(int month, int year, int... camperPlaceId) {
        int[] dataForChart = new int[camperPlaceId.length];
        CamperPlace camperPlace = new CamperPlace();

        if (month == 0) {
            for (int i = 0; i <= camperPlaceId.length - 1; i++) {
                dataForChart[i] = statisticsRepository.findByCamperPlace_IdAndYear(camperPlaceId[i], year).stream().mapToInt(statistics -> statistics.getReservationCount()).sum();

            }
        } else {
            for (int i = 0; i <= camperPlaceId.length - 1; i++) {
                dataForChart[i] = statisticsRepository.findByCamperPlace_IdAndMonthAndYear(camperPlaceId[i], month, year).getReservationCount();
            }
        }
        return dataForChart;
    }

    //TODO poprawic wyswietlanie dla wykresow z uzyciem tabeli statistics
    public double[] getRevenueForChart(int month, int year, int... camperPlaceId) {
        double[] dataForChart = new double[camperPlaceId.length];
        if (month == 0) {
            double revenue = 0;

            for (int i = 0; i <= camperPlaceId.length - 1; i++) {
                dataForChart[i] = statisticsRepository.findByCamperPlace_IdAndYear(camperPlaceId[i], year).stream().mapToDouble(statistics -> statistics.getRevenue()).sum();
            }

        } else {
            for (int i = 0; i <= camperPlaceId.length - 1; i++) {
                dataForChart[i] = statisticsRepository.findByCamperPlace_IdAndMonthAndYear(camperPlaceId[i], month, year).getRevenue();
            }
        }
        return dataForChart;
    }
    public int getReservationCountForCamperPlace(int id, int month, int year) {
        if (month == 0) {

            return statisticsRepository.findByCamperPlace_IdAndYear(id, year).stream().mapToInt(Statistics::getReservationCount).sum();

        } else {
            return statisticsRepository.findByCamperPlace_IdAndMonthAndYear(id, month, year).getReservationCount();

        }
    }
    public double getRevenueForCamperPlace(int id, int month, int year) {
        if (month == 0) {

            return statisticsRepository.findByCamperPlace_IdAndYear(id, year).stream().mapToDouble(Statistics::getRevenue).sum();

        } else {
            return statisticsRepository.findByCamperPlace_IdAndMonthAndYear(id, month, year).getRevenue();

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
        try {
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
        } catch (NonUniqueResultException e) {
            System.out.println("tu sie wyjebalo");
        }

    }

    @Transactional
    @Scheduled(cron = "0 0 0 1 1 *")
    public void generateAnnuallyCamperPlaceData() {
        generateStatisticsRecord();
    }

    @Transactional
    protected void generateStatisticsRecord() {
        try {
            for (int i = 1; i <= 12; i++) {
                int month = i;
                int year = LocalDate.now().getYear();
                camperPlaceService.findAllCamperPlaces().forEach(camperPlace -> {
                    newStatisticsInstance(camperPlace, month, year);
                });
            }
        } catch (Exception e) {

        }

    }
}
