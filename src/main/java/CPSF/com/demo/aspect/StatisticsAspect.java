package CPSF.com.demo.aspect;

import CPSF.com.demo.entity.Reservation;
import CPSF.com.demo.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class StatisticsAspect {

	private final StatisticsService statisticsService;

	@AfterReturning(
			pointcut = "execution(* CPSF.com.demo.controller.*.*(..))",
			returning = "response"
	)
	public void updateStatistics(Object response) {
		Reservation reservation;

		if (response instanceof ResponseEntity<?> entity && entity.getBody() instanceof Reservation) {
			reservation = (Reservation) entity.getBody();
			statisticsService.update(reservation, reservation.getCamperPlace());
		}
	}
}
