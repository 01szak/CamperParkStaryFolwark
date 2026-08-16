package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.service.processor.task.TaskFactory;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebAppController {

    private final TaskFactory taskFactory;

    @PostMapping("/reservation/init")
    @Parameter(in = ParameterIn.HEADER, name = "X-org-id", required = true)
    @Parameter(in = ParameterIn.HEADER, name = "X-api-key", required = true)
    public void sendAuthenticationEmail(@RequestBody ReservationDTO reservationDTO) {
        taskFactory.getWebAppReservationTask(reservationDTO).doTask();
    }

}
