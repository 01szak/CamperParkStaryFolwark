package CPSF.com.demo;

import CPSF.com.demo.model.constant.Country;
import CPSF.com.demo.model.constant.Operation;
import CPSF.com.demo.model.constant.ReservationStatus;
import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.model.dto.CamperPlace_DTO;
import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.model.dto.Reservation_DTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlaceType;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.service.core.CamperPlaceService;
import CPSF.com.demo.service.core.CamperPlaceTypeService;
import CPSF.com.demo.service.core.GuestService;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.core.SearchCriteria;
import CPSF.com.demo.service.core.StatisticsService;
import CPSF.com.demo.service.util.DtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsServiceIT extends BaseIT {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private CamperPlaceService camperPlaceService;
    @Autowired
    private CamperPlaceTypeService camperPlaceTypeService;
    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void shouldReturnRevenueCorrectly() {
        //Given
        final var dataEntries = 10;
        final var cpPrice = BigDecimal.valueOf(1);
        final var reservationCost = cpPrice; //reservation is one day long

        for (int i = 0; i < dataEntries; i++) {
            createReservationWithNewData(
                    "test_cpt" + i,
                    cpPrice,
                    String.valueOf(i),
                    "GUEST_DEMO" + i,
                    Country.PERU,
                    LocalDate.parse("2000-01-01"),
                    LocalDate.parse("2000-01-02"),
                    i <= 4
            );
        }

        //Then
        var revenue = statisticsService.getRevenue();
        assertThat(revenue.size()).isEqualTo(2)
                .withFailMessage("revenue size should contains element with paid and element with unpaid revenue reservations (size 2)");

        var revenueWithPaidReservations = revenue.get(0);
        var revenueWithUnPaidReservations = revenue.get(1);

        validateRevenues(revenueWithPaidReservations, dataEntries);
        validateRevenues(revenueWithUnPaidReservations, dataEntries);
    }

    @Test
    public void shouldReturnCorrectGuestPerCountryDistribution() {
        //Given (2 new guest different country with reservation)
        createReservationWithNewData("TYPE1",
                BigDecimal.valueOf(123),
                "1",
                "GUEST1",
                Country.POLAND,
                LocalDate.parse("2030-01-01"),
                LocalDate.parse("2030-01-05"),
                false
        );
        createReservationWithNewData(
                "TYPE2",
                BigDecimal.valueOf(123),
                "2",
                "GUEST2",
                Country.GERMANY,
                LocalDate.parse("2030-01-01"),
                LocalDate.parse("2030-01-05"),
                false
        );
        assertThat(statisticsService.getUserPerCountry(01, 2030).size() == 2).isTrue()
                .withFailMessage("country distribution should contains x count equal to distinct countries count");

        createReservation(
                camperPlaceService.findBy(new SearchCriteria("index", Operation.EQUALS, "1")).stream().findFirst().get(),
                LocalDate.parse("2030-01-06"),
                LocalDate.parse("2030-01-08"),
                guestService.findBy(new SearchCriteria("firstname", Operation.EQUALS, "GUEST1")).stream().findFirst().get(),
                false
        );
        assertThat(statisticsService.getUserPerCountry(01, 2030).get(0).usersCount() == 1).isTrue()
                .withFailMessage("if guest contains 2 reservations in given month distribution y's should be equal to number of guests (1)");
        assertThat(statisticsService.getUserPerCountry(01, 2030).get(1).usersCount() == 1).isTrue();
    }

    private Reservation createReservationWithNewData(String cpTypeName, BigDecimal cpTypePrice, String camperPlaceIndex, String guestFirstName, Country country, LocalDate checkin, LocalDate checkout, boolean paid) {
        var cpType = createCpType(cpTypeName, cpTypePrice);
        var cp = createCamperPlace(camperPlaceIndex, cpType);
        var guest = createGuest(guestFirstName, country);
        return createReservation(cp, checkin, checkout, guest, paid);
    }

    private Reservation createReservation(CamperPlace camperPlace, LocalDate checkin, LocalDate checkout, Guest guest1, boolean paid) {
        return reservationService.create(
                new Reservation_DTO(
                        null,
                        checkin,
                        checkout,
                        DtoMapper.getGuestDTO(guest1),
                        DtoMapper.getCamperPlaceDto(camperPlace),
                        paid,
                        ReservationStatus.COMING
                )
        );
    }

    private Guest createGuest(String guestFirstName, Country country) {
        return guestService.create(new GuestDTO(null, guestFirstName, null, null, null, null, country.getIsoCode()));
    }

    private CamperPlace createCamperPlace(String camperPlaceIndex, CamperPlaceType cpType) {
        return camperPlaceService.create(new CamperPlace_DTO(null, camperPlaceIndex, DtoMapper.getCamperPlaceTypeDTO(cpType), null));
    }

    private CamperPlaceType createCpType(String typeName, BigDecimal cpTypePrice) {
        return camperPlaceTypeService.create(new CamperPlaceTypeDTO(null, typeName, cpTypePrice));
    }

    private static void validateRevenues(
            List<StatisticsService.StatisticsModel.Revenue> rev,
            int dataEntries
    ) {
        assertThat(rev.size()).isEqualTo(dataEntries)
                .withFailMessage("paid and unpaid reservations are not distinguished correctly");
        for (int i = 0; i < dataEntries; i++) {
            assertThat(rev.get(i).revenue().stripTrailingZeros())
                    .withFailMessage("revenue should be either actual cost or 0")
                    .isIn(BigDecimal.ONE, BigDecimal.ZERO)
                    .withFailMessage("revenue should be either actual cost or 0");
            assertThat(rev.get(i).count())
                    .isIn(1L , 0L)
                    .withFailMessage("reservations count should be either 0 or actual count");
        }
    }

}
