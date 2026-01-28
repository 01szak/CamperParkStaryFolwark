package CPSF.com.demo.statisticServiceTest;

import CPSF.com.demo.DTO.StatisticsDTO;
import CPSF.com.demo.entity.CamperPlace;
import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.entity.Statistics;
import CPSF.com.demo.enums.CamperPlaceType;
import CPSF.com.demo.service.CamperPlaceService;
import CPSF.com.demo.service.implementation.ReservationServiceImpl;
import CPSF.com.demo.service.implementation.StatisticsServiceImpl;
import CPSF.com.demo.util.ReservationCalculator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static CPSF.com.demo.testUtil.TestUtil.reservationGenerator;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
public class StatisticsServiceTest {

	private final int year = 2025;

	private final int month = 1;

	private final int startDay = 1;

	private final int endDay = 10;

	private final LocalDate mockCheckin = LocalDate.of(year, month, startDay);

	private final LocalDate mockCheckout = LocalDate.of(year, month, endDay);

	private final ReservationServiceImpl reservationService = mock(ReservationServiceImpl.class);

	private final CamperPlaceService camperPlaceService = mock(CamperPlaceService.class);

	private final ReservationCalculator reservationCalculator = new ReservationCalculator();

	private final ArgumentCaptor<Statistics> captor = ArgumentCaptor.forClass(Statistics.class);


	private  CamperPlace mockCamperPlace
			= CamperPlace.builder().index("1").isOccupied(true).price(BigDecimal.valueOf(100)).camperPlaceType(CamperPlaceType.STANDARD).build();
	private  Reservation mockReservation
			=  Reservation.builder().checkin(mockCheckin).checkout(mockCheckout).paid(false).camperPlace(mockCamperPlace).build();

	private final StatisticsServiceImpl statisticsService = new StatisticsServiceImpl(
			reservationService,
			camperPlaceService,
			reservationCalculator
	);

	@BeforeEach
	public void setCamperPlaceReservation() {
		mockCamperPlace.setReservations(List.of(mockReservation));

	}

	@Test
	public void shouldCorrectlyGenerateStatisticsWithNoRevenueAndOneReservation() {
		when(camperPlaceService.findAll().toList()).thenReturn(List.of(mockCamperPlace));
		when(reservationService.findByMonthYearAndCamperPlaceIdIfPaid(any(Integer.class), any(Integer.class), any(Integer.class)))
				.thenReturn(mockCamperPlace.getReservations());
		Statistics expected = Statistics.builder()
				.year(year)
				.month(month)
				.camperPlace(mockCamperPlace)
				.revenue(0)
				.reservationCount(1)
				.build();

		List<Statistics> actual = statisticsService.generateStatistics(1, 2025);

		assertEquals(1, actual.size());
		assertEquals(expected, actual.get(0));
	}

	@Test
	public void shouldCorrectlyGenerateStatisticsWithNoRevenueAndMultipleReservation() {
		mockCamperPlace.setReservations(reservationGenerator(10,false, mockCamperPlace, month, year));
		when(camperPlaceService.findAll().toList()).thenReturn(List.of(mockCamperPlace));
		when(reservationService.findByMonthYearAndCamperPlaceIdIfPaid(any(Integer.class), any(Integer.class), any(Integer.class)))
				.thenReturn(mockCamperPlace.getReservations());
		Statistics expected = Statistics.builder()
				.year(year)
				.month(month)
				.camperPlace(mockCamperPlace)
				.revenue(0)
				.reservationCount(10)
				.build();

		List<Statistics> actual = statisticsService.generateStatistics(1, 2025);

		assertEquals(1, actual.size());
		assertEquals(expected, actual.get(0));
	}

	@Test
	public void shouldCountRevenueForSingleReservationCorrectly() {
		mockCamperPlace.setReservations(reservationGenerator(1,true, mockCamperPlace, month, year));
		when(camperPlaceService.findAll().toList()).thenReturn(List.of(mockCamperPlace));
		when(reservationService.findByMonthYearAndCamperPlaceIdIfPaid(any(Integer.class), any(Integer.class), any(Integer.class)))
				.thenReturn(mockCamperPlace.getReservations());
		Reservation generatedReservation = mockCamperPlace.getReservations().get(0);
		long daysInReservation = generatedReservation.getCheckin().datesUntil(generatedReservation.getCheckout().plusDays(1)).count();
		BigDecimal expectedRevenue = reservationCalculator.calculateFinalReservationCost(generatedReservation.getCheckin(), generatedReservation.getCheckout(), mockCamperPlace.getPrice());
		List<Statistics> actual = statisticsService.generateStatistics(1, 2025);

		assertEquals(1, actual.size());
		assertEquals(expectedRevenue, actual.get(0).getRevenue());
	}

	@Test
	public void shouldCountRevenueForMultipleReservationsCorrectly() {
		mockCamperPlace.setReservations(reservationGenerator(10,true, mockCamperPlace, month, year));
		when(camperPlaceService.findAll()).thenReturn(new PageImpl<>(List.of(mockCamperPlace)));
		when(reservationService.findByMonthYearAndCamperPlaceIdIfPaid(any(Integer.class), any(Integer.class), any(Integer.class)))
				.thenReturn(mockCamperPlace.getReservations());
		List<Reservation> generatedReservations = mockCamperPlace.getReservations();
		BigDecimal expectedRevenue = BigDecimal.ZERO;

		for (Reservation r : generatedReservations) {
			long daysInReservation = r.getCheckin().datesUntil(r.getCheckout().plusDays(1)).count();
			expectedRevenue = expectedRevenue.add(reservationCalculator.calculateFinalReservationCost(r.getCheckin(), r.getCheckout(), mockCamperPlace.getPrice()));
		}

		List<Statistics> actual = statisticsService.generateStatistics(1, 2025);

		assertEquals(1, actual.size());
		assertEquals(expectedRevenue, actual.get(0).getRevenue());
	}

	@Test
	public void shouldSendCorrectRevenueData() {
		Reservation generatedReservation = reservationGenerator(1,true, mockCamperPlace, month, year).get(0);
		mockCamperPlace.setReservations(List.of(generatedReservation));
		when(camperPlaceService.findAll().toList()).thenReturn(List.of(mockCamperPlace));
		when(reservationService.findByMonthYearAndCamperPlaceIdIfPaid(any(Integer.class), any(Integer.class),any(Integer.class)))
				.thenReturn(mockCamperPlace.getReservations());

		List<StatisticsDTO> actual = statisticsService.getStatisticsDTOWithRevenue(month, year);

		StatisticsDTO expected = new StatisticsDTO(
				generatedReservation.getCamperPlace().getIndex(),
				reservationCalculator.calculateFinalReservationCost(generatedReservation.getCheckin(), generatedReservation.getCheckout(), mockCamperPlace.getPrice()).doubleValue());
		assertEquals(expected, actual.get(0));
	}

}





