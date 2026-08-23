package CPSF.com.demo.controller;

import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.processor.TaskService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static CPSF.com.demo.model.constant.TaskType.WEB_APP_RESERVATION_TASK;

@RestController
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebAppController {

    private final TaskService taskService;

    @PostMapping("/reservation/init")
    @Parameter(in = ParameterIn.HEADER, name = "X-org-id", required = true)
    @Parameter(in = ParameterIn.HEADER, name = "X-api-key", required = true)
    public void sendAuthenticationEmail(@RequestBody ReservationDTO reservationDTO) {
        final var task = Task.builder()
                .targetId(UUID.randomUUID().toString())
                .payload(reservationDTO)
                .taskType(WEB_APP_RESERVATION_TASK)
                .parentTask(null)
                .build();
        taskService.create(task);
    }

}
