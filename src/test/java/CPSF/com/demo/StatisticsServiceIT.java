package CPSF.com.demo;

import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlaceType;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.repository.CamperPlaceRepository;
import CPSF.com.demo.repository.CamperPlaceTypeRepository;
import CPSF.com.demo.repository.GuestRepository;
import CPSF.com.demo.repository.ReservationRepository;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.core.StatisticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsServiceIT extends BaseIT {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private CamperPlaceRepository camperPlaceRepository;

    @Autowired
    private CamperPlaceTypeRepository camperPlaceTypeRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StatisticsService statisticsService;

    private static final int RESERVATION_COUNT = 10;
    private static final int CAMPER_PLACE_COUNT = 10;
    private static final BigDecimal CAMPER_PLACE_PRICE = BigDecimal.valueOf(100);

    private static BigDecimal reservationPrice = BigDecimal.ZERO;

    private void createDummyReservations(int reservationCount, BigDecimal camperPlacePrice) {
        var reservations = new ArrayList<Reservation>();
        var checkin = LocalDate.of(2137, 6, 7);
        var checkout = checkin.plusDays(2);
        var cpt = camperPlaceTypeRepository.save(CamperPlaceType.builder().typeName("test").price(camperPlacePrice).build());
        var guest = guestRepository.save(Guest.builder().firstname("guest").build());
        for (var i = 0; i < reservationCount; i++) {
            var cp = camperPlaceRepository.save(CamperPlace.builder().index(String.valueOf(i)).camperPlaceType(cpt).build());
            reservationPrice = BigDecimal.valueOf(cp.getPrice().intValue() * 3L);
            reservations.add(
                    Reservation.builder()
                            .checkin(checkin)
                            .checkout(checkout)
                            .camperPlace(cp)
                            .guest(guest)
                            .price(reservationPrice)
                            .paid(i % 2 == 0)
                            .build()
            );
        }

        reservationRepository.saveAll(reservations);
        assertThat(reservationService.findAll().getSize()).isEqualTo(RESERVATION_COUNT);
        assertThat(camperPlaceRepository.findAll().size()).isEqualTo(CAMPER_PLACE_COUNT);
        assertThat(guestRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void shouldReturnRevenueCorrectly() {
        //Given
        createDummyReservations(RESERVATION_COUNT, CAMPER_PLACE_PRICE);

        //Then
        var revenue = statisticsService.getRevenue();
        assertThat(revenue.size()).isEqualTo(2);

        var revenueWithPaidReservations = revenue.getFirst();
        var revenueWithUnPaidReservations = revenue.get(1);
        validateRevenues(revenueWithPaidReservations, revenueWithUnPaidReservations);
    }

    private static void validateRevenues(
            List<StatisticsService.StatisticsModel.Revenue> paid,
            List<StatisticsService.StatisticsModel.Revenue> unPaid
    ) {
        assertThat(paid.size()).isEqualTo(unPaid.size());

        assertThat(revenueIsZeroOrReservationPrice(paid)).isTrue();

        assertThat(revenueIsZeroOrReservationPrice(unPaid)).isTrue();

        for (int i = 0; i < paid.size(); i++) {
            assertThat(paid.get(i).cpIndex().equals(unPaid.get(i).cpIndex())).isTrue();
            assertThat(paid.get(i).revenue().compareTo(unPaid.get(i).revenue()) != 0).isTrue();
            assertThat(paid.get(i).count() != unPaid.get(i).count()).isTrue();
        }
    }

    private static boolean revenueIsZeroOrReservationPrice(List<StatisticsService.StatisticsModel.Revenue> paid) {
        return paid.stream().allMatch(r ->
                r.revenue().compareTo(BigDecimal.ZERO) == 0 || r.revenue().compareTo(reservationPrice) == 0);
    }

}
