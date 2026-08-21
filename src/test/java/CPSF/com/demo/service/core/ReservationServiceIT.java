package CPSF.com.demo.service.core;

import CPSF.com.demo.BaseIT;
import CPSF.com.demo.exception.UserInputException;
import CPSF.com.demo.helper.AuthenticationHelper;
import CPSF.com.demo.model.constant.Country;
import CPSF.com.demo.model.constant.ReservationStatus;
import CPSF.com.demo.model.constant.UserRole;
import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.entity.User;
import CPSF.com.demo.service.util.DtoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static CPSF.com.demo.helper.AuthenticationHelper.IT_USER_LOGIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReservationServiceIT extends BaseIT {

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUpSecurity() {
        final var testUser = userService.create(User.builder()
                .login(IT_USER_LOGIN)
                .username(IT_USER_LOGIN)
                .email("it_test_user@example.com")
                .password("testPassword")
                .userRole(UserRole.ADMIN)
                .build()
        );
        AuthenticationHelper.authenticateUser(testUser);
    }

    @AfterEach
    public void tearDownSecurity() {
        AuthenticationHelper.clearAuthentication();
    }

    @Test
    public void shouldPersistAndRetrieveReservationInDatabase() {
        // Given
        final var checkin = LocalDate.parse("2030-05-01");
        final var checkout = LocalDate.parse("2030-05-05");
        final var cpPrice = BigDecimal.valueOf(50);

        final var createdReservation = createReservationWithNewData(
                "IT_TYPE_1",
                cpPrice,
                "IT_CP_1",
                "Jan",
                "Kowalski",
                Country.POLAND,
                checkin,
                checkout,
                false
        );

        // When
        final var foundReservation = reservationService.findById(createdReservation.getId());

        // Then
        assertThat(foundReservation).isNotNull();
        assertThat(foundReservation.getId()).isEqualTo(createdReservation.getId());
        assertThat(foundReservation.getCheckin()).isEqualTo(checkin);
        assertThat(foundReservation.getCheckout()).isEqualTo(checkout);
        assertThat(foundReservation.getPaid()).isFalse();
        assertThat(foundReservation.getReservationStatus()).isEqualTo(ReservationStatus.COMING);
        assertThat(foundReservation.getCamperPlace().getIndex()).isEqualTo("IT_CP_1");
        assertThat(foundReservation.getGuest().getFirstname()).isEqualTo("Jan");
        assertThat(foundReservation.getGuest().getLastname()).isEqualTo("Kowalski");
        assertThat(foundReservation.getCreator().getLogin()).isEqualTo(IT_USER_LOGIN);
        assertThat(foundReservation.getPrice()).isPositive();
    }

    @Test
    public void shouldCreateReservationWithExistingGuest() {
        // Given
        final var guest = createGuest("Existing", "Guest", Country.POLAND);
        final var cpType = createCpType("IT_TYPE_2", BigDecimal.valueOf(60));
        final var cp = createCamperPlace("IT_CP_2", cpType);
        final var checkin = LocalDate.parse("2030-06-01");
        final var checkout = LocalDate.parse("2030-06-04");

        final var reservationDto = new ReservationDTO(
                null,
                checkin,
                checkout,
                DtoMapper.getGuestDTO(guest),
                DtoMapper.getCamperPlaceDto(cp),
                true,
                ReservationStatus.COMING
        );

        // When
        final var createdReservation = reservationService.create(reservationDto);

        // Then
        assertThat(createdReservation).isNotNull();
        assertThat(createdReservation.getGuest().getId()).isEqualTo(guest.getId());
        assertThat(createdReservation.getPaid()).isTrue();

        final var retrievedGuest = guestService.findById(guest.getId());
        assertThat(retrievedGuest.getId()).isEqualTo(guest.getId());
    }

    @Test
    public void shouldRejectOverlappingReservationUsingDbOccupiedDates() {
        // Given
        final var cpType = createCpType("IT_TYPE_3", BigDecimal.valueOf(70));
        final var cp = createCamperPlace("IT_CP_3", cpType);
        final var guest1 = createGuest("Guest", "One", Country.POLAND);
        final var guest2 = createGuest("Guest", "Two", Country.GERMANY);

        final var checkin1 = LocalDate.parse("2030-07-01");
        final var checkout1 = LocalDate.parse("2030-07-06");
        createReservation(cp, checkin1, checkout1, guest1, false);

        // When & Then - overlapping check-in
        final var overlappingDto = new ReservationDTO(
                null,
                LocalDate.parse("2030-07-03"),
                LocalDate.parse("2030-07-08"),
                DtoMapper.getGuestDTO(guest2),
                DtoMapper.getCamperPlaceDto(cp),
                false,
                ReservationStatus.COMING
        );

        assertThatThrownBy(() -> reservationService.create(overlappingDto))
                .isInstanceOf(UserInputException.class)
                .hasMessage("Parcela jest już zajęta!");
    }

    @Test
    public void shouldUpdateReservationInDatabaseSuccessfully() {
        // Given
        final var initialCheckin = LocalDate.parse("2030-08-01");
        final var initialCheckout = LocalDate.parse("2030-08-05");
        final var reservation = createReservationWithNewData(
                "IT_TYPE_4",
                BigDecimal.valueOf(80),
                "IT_CP_4",
                "Piotr",
                "Zieliński",
                Country.POLAND,
                initialCheckin,
                initialCheckout,
                false
        );

        final var newCheckin = LocalDate.parse("2030-08-10");
        final var newCheckout = LocalDate.parse("2030-08-16");
        final var updatedGuestDto = new GuestDTO(
                reservation.getGuest().getId(),
                "Piotr-Updated",
                "Zieliński-Updated",
                null,
                null,
                null,
                Country.POLAND.getIsoCode()
        );

        final var updateDto = new ReservationDTO(
                reservation.getId(),
                newCheckin,
                newCheckout,
                updatedGuestDto,
                DtoMapper.getCamperPlaceDto(reservation.getCamperPlace()),
                true,
                ReservationStatus.COMING
        );

        // When
        reservationService.update(updateDto);

        // Then
        final var updatedReservation = reservationService.findById(reservation.getId());
        assertThat(updatedReservation.getCheckin()).isEqualTo(newCheckin);
        assertThat(updatedReservation.getCheckout()).isEqualTo(newCheckout);
        assertThat(updatedReservation.getPaid()).isTrue();
        assertThat(updatedReservation.getGuest().getFirstname()).isEqualTo("Piotr-Updated");
        assertThat(updatedReservation.getGuest().getLastname()).isEqualTo("Zieliński-Updated");
    }

    @Test
    public void shouldRejectUpdateWhenOverlappingAnotherReservationInDatabase() {
        // Given
        final var cpType = createCpType("IT_TYPE_5", BigDecimal.valueOf(90));
        final var cp = createCamperPlace("IT_CP_5", cpType);
        final var guest1 = createGuest("Adam", "Pierwszy", Country.POLAND);
        final var guest2 = createGuest("Ewa", "Druga", Country.POLAND);

        final var res1 = createReservation(cp, LocalDate.parse("2030-09-01"), LocalDate.parse("2030-09-06"), guest1, false);
        final var res2 = createReservation(cp, LocalDate.parse("2030-09-10"), LocalDate.parse("2030-09-15"), guest2, false);

        // When & Then - try updating res2 to overlap res1
        final var conflictingUpdateDto = new ReservationDTO(
                res2.getId(),
                LocalDate.parse("2030-09-04"),
                LocalDate.parse("2030-09-12"),
                DtoMapper.getGuestDTO(guest2),
                DtoMapper.getCamperPlaceDto(cp),
                false,
                ReservationStatus.COMING
        );

        assertThatThrownBy(() -> reservationService.update(conflictingUpdateDto))
                .isInstanceOf(UserInputException.class)
                .hasMessage("Parcela jest już zajęta!");
    }
}
