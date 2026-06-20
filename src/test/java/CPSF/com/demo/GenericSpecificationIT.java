package CPSF.com.demo;
import CPSF.com.demo.model.constant.Country;
import CPSF.com.demo.model.entity.Reservation;
import org.junit.jupiter.api.AfterEach;
import CPSF.com.demo.model.constant.Operation;
import CPSF.com.demo.service.core.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GenericSpecificationIT extends BaseIT {

    private static final String CP_TYPE = "type";
    private static final BigDecimal CP_TYPE_PRICE = BigDecimal.valueOf(123);
    private static final String CP_INDEX = "1u";
    private static final String GUEST_FN = "guestFirstname";
    private static final String GUEST_LN = "guestLastname";
    private static final Country COUNTRY = Country.POLAND;
    private static final LocalDate CHECKIN = LocalDate.parse("3026-01-01");
    private static final LocalDate CHECKOUT = LocalDate.parse("3026-01-10");
    private static final boolean IS_PAID = true;
    private static final String GUEST_LN_2 = "guestLastname_2";

    private static Reservation reservation = null;

    @BeforeEach
    void prepareData() {
        reservation = createReservationWithNewData(
                CP_TYPE,
                CP_TYPE_PRICE,
                CP_INDEX,
                GUEST_FN,
                GUEST_LN,
                COUNTRY,
                CHECKIN,
                CHECKOUT,
                IS_PAID
        );
    }

    @AfterEach
    void deleteData() {
        reservationService.delete(reservation);
    }

    @Test
    void shouldReturnDataWithEqualOperation() {
        // given
        var criteria = new SearchCriteria("firstname", Operation.EQUALS, GUEST_FN);

        // when
        var result = guestService.findBy(criteria).get().toList();

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getFirstname()).isEqualTo(GUEST_FN);
    }

    @Test
    void shouldReturnDataFilteredByMultipleCriteria() {
        // given
        var criteria = new SearchCriteria("firstname", Operation.EQUALS, GUEST_FN);
        var criteria2 = new SearchCriteria("lastname", Operation.NOT_EQUALS, GUEST_LN_2);

        // when
        var result = guestService.findBy(criteria, criteria2).get().toList();

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getFirstname()).isEqualTo(GUEST_FN);
    }


    @Test
    void shouldReturnDataFilteredByLessThenOperation() {
        // given
        var criteria = new SearchCriteria("checkin", Operation.LESS_THEN, CHECKIN.plusDays(1).toString());

        // when
        var result = reservationService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.stream().filter(r -> Objects.equals(reservation.getId(), r.getId())).count()).isEqualTo(1);
    }

    @Test
    void shouldReturnDataFilteredByGreaterThenOperation() {
        // given
        var criteria = new SearchCriteria("checkin", Operation.GREATER_THEN, CHECKIN.minusDays(1).toString());

        // when
        var result = reservationService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.stream().filter(r -> Objects.equals(reservation.getId(), r.getId())).count()).isEqualTo(1);
    }

    @Test
    void shouldReturnDataFilteredByBetweenOperation() {
        // given
        var criteria = new SearchCriteria(
                "checkin",
                Operation.BETWEEN,
                CHECKIN.minusDays(1).toString(),
                CHECKOUT.plusDays(1).toString()
        );

        // when
        var result = reservationService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.stream().filter(r -> Objects.equals(r.getId(), reservation.getId())).count()).isEqualTo(1);
    }

    @Test
    void shouldReturnDataFilteredByLikeOperation() {
        // given
        var criteria = new SearchCriteria("firstname", Operation.LIKE, "stFir");

        // when
        var result = guestService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.stream().filter(g -> GUEST_FN.equals(g.getFirstname())).count()).isEqualTo(1);
    }

    @Test
    void shouldReturnDataWithJoinAndMultipleCriteria() {
        // given
        var criteria = new SearchCriteria("guest","firstname", Operation.LIKE, GUEST_FN);
        var criteria2 = new SearchCriteria("guest","lastname", Operation.LIKE, GUEST_LN);

        // when
        var result = reservationService.findBy(criteria, criteria2).get().toList();

        // then
        assertThat(result.size()).isEqualTo(1);

        var actuallGuest = result.getFirst().getGuest();
        assertThat(GUEST_FN.equals(actuallGuest.getFirstname()));
    }

    @Test
    void shouldReturnDataFilteredByBigDecimalOperation() {
        // given
        var criteria = new SearchCriteria("price", Operation.EQUALS, reservation.getPrice().toString());

        // when
        var result = reservationService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.stream().anyMatch(r -> Objects.equals(r.getId(), reservation.getId()))).isTrue();
    }

    @Test
    void shouldReturnDataFilteredByBooleanOperation() {
        // given
        var criteria = new SearchCriteria("paid", Operation.EQUALS, String.valueOf(IS_PAID));

        // when
        var result = reservationService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.stream().anyMatch(r -> Objects.equals(r.getId(), reservation.getId()))).isTrue();
    }

    @Test
    void shouldReturnDataFilteredByIntegerOperation() {
        // given
        var criteria = new SearchCriteria("id", Operation.EQUALS, reservation.getId().toString());

        // when
        var result = reservationService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getId()).isEqualTo(reservation.getId());
    }

    @Test
    void shouldReturnDataFilteredByEnumOperation() {
        // given
        var criteria = new SearchCriteria("reservationStatus", Operation.EQUALS, reservation.getReservationStatus().name());

        // when
        var result = reservationService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.stream().anyMatch(r -> Objects.equals(r.getId(), reservation.getId()))).isTrue();
    }

    @Test
    void shouldReturnDataWithNotEqualOperation() {
        // given
        var criteria = new SearchCriteria("firstname", Operation.NOT_EQUALS, "NonExistentName");

        // when
        var result = guestService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.stream().anyMatch(g -> GUEST_FN.equals(g.getFirstname()))).isTrue();
    }

    @Test
    void shouldMatchLikeOperationCaseInsensitively() {
        // given
        var criteria = new SearchCriteria("firstname", Operation.LIKE, "GuEsTfIrStNaMe");

        // when
        var result = guestService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getFirstname()).isEqualTo(GUEST_FN);
    }

    @Test
    void shouldReturnEmptyWhenNoMatchesFound() {
        // given
        var criteria = new SearchCriteria("firstname", Operation.EQUALS, "ThisNameDoesNotExistInAnyRecord");

        // when
        var result = guestService.findBy(criteria).get().toList();

        // then
        assertThat(result.size()).isEqualTo(0);
    }

}
