package CPSF.com.demo;

import CPSF.com.demo.model.constant.Country;
import CPSF.com.demo.model.constant.ReservationStatus;
import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.dto.camperPlaceDTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlaceType;
import CPSF.com.demo.model.entity.Guest;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.service.core.CamperPlaceService;
import CPSF.com.demo.service.core.CamperPlaceTypeService;
import CPSF.com.demo.service.core.GuestService;
import CPSF.com.demo.service.core.ReservationService;
import CPSF.com.demo.service.core.StatisticsService;
import CPSF.com.demo.service.util.DtoMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = CamperparkdemoApplication.class)
@ActiveProfiles("test")
@Transactional
@Testcontainers
public class BaseIT {

    @Autowired
    protected ReservationService reservationService;
    @Autowired
    protected GuestService guestService;
    @Autowired
    protected CamperPlaceService camperPlaceService;
    @Autowired
    protected CamperPlaceTypeService camperPlaceTypeService;
    @Autowired
    protected StatisticsService statisticsService;

    private static long eachTestStart;
    private static long testStart;

    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("test_camper_park_sf")
            .withUsername("root")
            .withPassword("qwer");

    static {
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

    @BeforeAll
    public static void beforeAll() {
        testStart = new Date().getTime();
    }

    @AfterAll
    public static void afterAll() {
        var testTime = (new Date().getTime() - testStart);
        System.out.printf("\nTOOK OVERALL: %s ms\n", testTime);
        MY_SQL_CONTAINER.stop();
    }

    @BeforeEach
    public void before() {
        eachTestStart = new Date().getTime();
        System.out.println("\n-----------< TEST START >-----------");
    }

    @AfterEach
    public void after() {
        var testTime = (new Date().getTime() - eachTestStart);
        System.out.println("\n-----------< TEST END >-----------");
        System.out.printf("TOOK: %s ms", testTime);
    }

    @Test
    public void isContainerRunning() {
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
    }

    protected Reservation createReservationWithNewData(
            String cpTypeName,
            BigDecimal cpTypePrice,
            String camperPlaceIndex,
            String guestFirstName,
            String guestLastName,
            Country country,
            LocalDate checkin,
            LocalDate checkout,
            boolean paid
    ) {
        var cpType = createCpType(cpTypeName, cpTypePrice);
        var cp = createCamperPlace(camperPlaceIndex, cpType);
        var guest = createGuest(guestFirstName, guestLastName, country);
        return createReservation(cp, checkin, checkout, guest, paid);
    }

    protected Reservation createReservation(
            CamperPlace camperPlace,
            LocalDate checkin,
            LocalDate checkout,
            Guest guest,
            boolean paid
    ) {
        return reservationService.create(
                new ReservationDTO(
                        null,
                        checkin,
                        checkout,
                        DtoMapper.getGuestDTO(guest),
                        DtoMapper.getCamperPlaceDto(camperPlace),
                        paid,
                        ReservationStatus.COMING
                )
        );
    }

    protected Guest createGuest(String guestFirstName, String guestLastName, Country country) {
        return guestService.create(
                new GuestDTO(
                        null,
                        guestFirstName,
                        guestLastName,
                        null,
                        null,
                        null,
                        country.getIsoCode()
                )
        );
    }

    protected CamperPlace createCamperPlace(String camperPlaceIndex, CamperPlaceType cpType) {
        return camperPlaceService.create(
                new camperPlaceDTO(
                        null,
                        camperPlaceIndex,
                        DtoMapper.getCamperPlaceTypeDTO(cpType),
                        null
                )
        );
    }

    protected CamperPlaceType createCpType(String typeName, BigDecimal cpTypePrice) {
        return camperPlaceTypeService.create(new CamperPlaceTypeDTO(null, typeName, cpTypePrice));
    }

}
