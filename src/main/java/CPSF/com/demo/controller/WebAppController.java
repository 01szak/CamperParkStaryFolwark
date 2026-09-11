package CPSF.com.demo.controller;

import CPSF.com.demo.model.constant.TaskType;
import CPSF.com.demo.model.dto.ReservationDTO;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.model.entity.User;
import CPSF.com.demo.service.core.UserService;
import CPSF.com.demo.service.processor.TaskService;
import CPSF.com.demo.service.util.DtoMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final UserService userService;

    @PostMapping("/reservation/init")
    @Parameter(in = ParameterIn.HEADER, name = "X-org-id", required = true)
    @Parameter(in = ParameterIn.HEADER, name = "X-api-key", required = true)
    public void createUnverifiedReservationAndSendAuthenticationEmail(@RequestBody @Valid ReservationDTO reservationDTO) {
        final var webAppUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        final var webAppUSer = (User) userService.loadUserByUsername(webAppUserName);
        final var task = Task.builder()
                .targetId(UUID.randomUUID().toString())
                .payload(reservationDTO.toBuilder().creator(DtoMapper.getUserDTO(webAppUSer)).build())
                .taskType(WEB_APP_RESERVATION_TASK)
                .parentTask(null)
                .build();
        taskService.create(task);
    }

    @PostMapping("/reservation/verify/{targetId}")
    @Parameter(in = ParameterIn.HEADER, name = "X-org-id", required = true)
    @Parameter(in = ParameterIn.HEADER, name = "X-api-key", required = true)
    public void verifyReservation(@PathVariable @NotNull String targetId) {
        final var task = Task.builder().targetId(targetId).taskType(TaskType.RESERVATION_STATUS_VERIFIER_TASK).build();
        taskService.create(task);
    }

}
