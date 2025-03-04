package CPSF.com.demo.service;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class StatisticsService {

    @Autowired
    ReservationRepository reservationRepository;

    public int reservationCount(int camperPlaceId, int month, int year) {
        if (month == 0) {
            return reservationRepository.findReservationByCamperPlace_IdAndAndCheckin_Year(camperPlaceId, year).size();

        }
        return reservationRepository.findReservationByCamperPlace_IdAndCheckin_MonthAndAndCheckin_Year(camperPlaceId, month, year).size();
    }

    public double revenueCount(int camperPlaceId, int month, int year) {
        if (month == 0) {
            List<Reservation> reservations = reservationRepository.findReservationByCamperPlace_IdAndAndCheckin_Year(camperPlaceId, year);
            return reservations.stream().mapToDouble(Reservation::calculateFinalPrice).sum();
        }
        List<Reservation> reservations = reservationRepository.findReservationByCamperPlace_IdAndCheckin_MonthAndAndCheckin_Year(camperPlaceId, month, year);
        return reservations.stream().mapToDouble(Reservation::calculateFinalPrice).sum();
    }

    public int[] getReservationCountForChart(int month, int year, int... camperPlaceId) {
        int[] dataForChart = new int[camperPlaceId.length ];
        for (int i = 0; i <= camperPlaceId.length - 1; i++) {
            dataForChart[i] = (reservationCount(camperPlaceId[i], month, year));

        }
        return dataForChart;
    }

    public double[] getRevenueForChart(int month, int year, int... camperPlaceId) {
        double[] dataForChart = new double[camperPlaceId.length ];
        for (int i = 0; i <= camperPlaceId.length - 1; i++) {
            dataForChart[i] = (revenueCount(camperPlaceId[i], month, year));

        }
        return dataForChart;
    }


}
