package CPSF.com.demo.service.core;

import CPSF.com.demo.BaseIT;
import CPSF.com.demo.exception.ClientSideException;
import CPSF.com.demo.exception.UserInputException;
import CPSF.com.demo.model.constant.Country;
import CPSF.com.demo.model.constant.JoinOperator;
import CPSF.com.demo.model.constant.Operation;
import CPSF.com.demo.model.entity.Reservation;
import CPSF.com.demo.model.entity.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static CPSF.com.demo.model.constant.TaskType.WEB_APP_RESERVATION_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(GUEST_FN.equals(actuallGuest.getFirstname())).isTrue();
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

    // --- Multiple Joining Operators Tests (a OR b OR c, (a AND b) OR c, (y AND z) OR x) ---

    @Test
    void shouldReturnDataWithAllOrJoinOperators() {
        // given (a or b or c)
        final var guestA = createGuest("Alice", "Alpha", Country.POLAND);
        final var guestB = createGuest("Bob", "Beta", Country.POLAND);
        final var guestC = createGuest("Charlie", "Gamma", Country.POLAND);

        final var criteriaA = new SearchCriteria("firstname", Operation.EQUALS, "Alice");
        final var criteriaB = new SearchCriteria("firstname", Operation.EQUALS, "Bob", JoinOperator.OR);
        final var criteriaC = new SearchCriteria("firstname", Operation.EQUALS, "Charlie", JoinOperator.OR);

        // when
        final var result = guestService.findBy(criteriaA, criteriaB, criteriaC).getContent();

        // then
        final var firstNames = result.stream().map(g -> g.getFirstname()).toList();
        assertThat(firstNames).contains("Alice", "Bob", "Charlie");
    }

    @Test
    void shouldReturnDataWithAndThenOrJoinOperators() {
        // given ((a and b) or c)
        final var guestA = createGuest("David", "Delta", Country.POLAND);
        final var guestB = createGuest("Eva", "Echo", Country.POLAND);

        // Subcase 1: Matching (David AND Delta) OR (NonExistent)
        final var criteria1A = new SearchCriteria("firstname", Operation.EQUALS, "David");
        final var criteria1B = new SearchCriteria("lastname", Operation.EQUALS, "Delta", JoinOperator.AND);
        final var criteria1C = new SearchCriteria("firstname", Operation.EQUALS, "NonExistent", JoinOperator.OR);

        final var result1 = guestService.findBy(criteria1A, criteria1B, criteria1C).getContent();
        assertThat(result1.stream().anyMatch(g -> "David".equals(g.getFirstname()) && "Delta".equals(g.getLastname()))).isTrue();

        // Subcase 2: Matching (David AND WrongLastName) OR (Eva) -> (false) OR true -> returns Eva
        final var criteria2A = new SearchCriteria("firstname", Operation.EQUALS, "David");
        final var criteria2B = new SearchCriteria("lastname", Operation.EQUALS, "WrongLastName", JoinOperator.AND);
        final var criteria2C = new SearchCriteria("firstname", Operation.EQUALS, "Eva", JoinOperator.OR);

        final var result2 = guestService.findBy(criteria2A, criteria2B, criteria2C).getContent();
        final var result2Names = result2.stream().map(g -> g.getFirstname()).toList();
        assertThat(result2Names).contains("Eva");
        assertThat(result2Names).doesNotContain("David");
    }

    @Test
    void shouldReturnDataWithXOrYAndZJoinOperators() {
        // given (x or (y and z)) structured as (y and z) or x
        final var guestY = createGuest("Frank", "Foxtrot", Country.POLAND);
        final var guestX = createGuest("Grace", "Golf", Country.POLAND);

        // (Frank AND Foxtrot) OR Grace
        final var criteriaY = new SearchCriteria("firstname", Operation.EQUALS, "Frank");
        final var criteriaZ = new SearchCriteria("lastname", Operation.EQUALS, "Foxtrot", JoinOperator.AND);
        final var criteriaX = new SearchCriteria("firstname", Operation.EQUALS, "Grace", JoinOperator.OR);

        final var result = guestService.findBy(criteriaY, criteriaZ, criteriaX).getContent();
        final var matchedNames = result.stream().map(g -> g.getFirstname()).toList();
        assertThat(matchedNames).contains("Frank", "Grace");
    }

    @Test
    void shouldThrowClientSideExceptionWhenSearchingByInvalidProperty() {
        // given
        final var criteria = new SearchCriteria("nonExistentProperty", Operation.EQUALS, "value");

        // when & then
        assertThatThrownBy(() -> guestService.findBy(criteria))
                .isInstanceOf(ClientSideException.class);
    }

    @Test
    void shouldThrowClientSideExceptionWhenJoiningInvalidRelation() {
        // given
        final var criteria = new SearchCriteria("invalidRelation", "someField", Operation.EQUALS, "value");

        // when & then
        assertThatThrownBy(() -> reservationService.findBy(criteria))
                .isInstanceOf(ClientSideException.class);
    }

    @Test
    void shouldThrowUserInputExceptionWhenDateFormatIsInvalid() {
        // given
        final var criteria = new SearchCriteria("checkin", Operation.EQUALS, "invalid-date-format");

        // when & then
        assertThatThrownBy(() -> reservationService.findBy(criteria))
                .isInstanceOf(UserInputException.class)
                .hasMessage("Nieprawidłowa data!");
    }

    @Test
    void shouldThrowClientSideExceptionWhenBetweenOperationHasNullSecondValue() {
        // given
        final var criteria = new SearchCriteria(null, "checkin", Operation.BETWEEN, "3026-01-01", null, null);

        // when & then
        assertThatThrownBy(() -> reservationService.findBy(criteria))
                .isInstanceOf(ClientSideException.class)
                .hasMessage("second values cannot be null while using between operation");
    }

    @Test
    void shouldReturnDataFilteredByNumericBetweenOperation() {
        // given
        final var criteria = new SearchCriteria(
                "price",
                Operation.BETWEEN,
                "1.00",
                "100000.00"
        );

        // when
        final var result = reservationService.findBy(criteria).getContent();

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.stream().anyMatch(r -> Objects.equals(r.getId(), reservation.getId()))).isTrue();
    }

    @Test
    void shouldReturnDataFilteredByNumericGreaterThanAndLessThan() {
        // given
        final var criteriaGreater = new SearchCriteria("price", Operation.GREATER_THEN, "1.00");
        final var criteriaLess = new SearchCriteria("price", Operation.LESS_THEN, "100000.00", JoinOperator.AND);

        // when
        final var result = reservationService.findBy(criteriaGreater, criteriaLess).getContent();

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.stream().anyMatch(r -> Objects.equals(r.getId(), reservation.getId()))).isTrue();
    }

    @Test
    void shouldReturnDataFilteredByLocalDateTimeGreaterThanAndLessThan() {
        // given
        final var task = taskService.create(Task.builder().taskType(WEB_APP_RESERVATION_TASK).targetId("dummyID").executionDate(LocalDateTime.parse("2067-01-02T01:00:00")).build());

        final var criteriaGreater = new SearchCriteria("executionDate", Operation.GREATER_THEN, "2067-01-01T01:00:00");
        final var criteriaLess = new SearchCriteria("executionDate", Operation.LESS_THEN, "2067-01-03T01:00:00", JoinOperator.AND);

        // when
        final var result = (Task) taskService.findBy(criteriaGreater, criteriaLess).get().findFirst().get();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(task.getId());
    }

    @Test
    void shouldReturnAllRecordsWhenCriteriaArrayIsEmpty() {
        // when
        final var result = guestService.findBy().getContent();

        // then
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldReturnAllRecordsWhenCriteriaHasEmptyKey() {
        // given
        final var criteria = new SearchCriteria("", Operation.EQUALS, "someValue");

        // when
        final var result = guestService.findBy(criteria).getContent();

        // then
        assertThat(result).isNotEmpty();
    }
}
