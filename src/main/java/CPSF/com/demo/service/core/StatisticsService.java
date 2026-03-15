package CPSF.com.demo.service.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ReservationService reservationService;
    private final CamperPlaceService camperPlaceService;


    public static class StatisticsModel {
        public record Revenue (String cpIndex, long count, BigDecimal revenue) {}
    }

    public List<List<StatisticsModel.Revenue>> getRevenue() {
        return getRevenue(0, 0);
    }

    public List<List<StatisticsModel.Revenue>> getRevenue(int month, int year) {
        return List.of(
                reservationService.countRevenueOfAllCamperPlaces(true, month, year),
                reservationService.countRevenueOfAllCamperPlaces(false, month, year)
        );
    }

}
