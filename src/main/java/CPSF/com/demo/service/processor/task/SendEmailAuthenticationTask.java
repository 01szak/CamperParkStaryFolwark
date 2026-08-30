package CPSF.com.demo.service.processor.task;

import CPSF.com.demo.model.EmailData;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.notification.NovuWorkflow;
import co.novu.Novu;
import co.novu.models.components.SubscriberPayloadDto;
import co.novu.models.components.TriggerEventRequestDtoTo2;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;


@RequiredArgsConstructor
public class SendEmailAuthenticationTask implements ExecutableTask {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Novu novu;
    private final Task sendEmailAuthenticationTaskEntity;

    @Override
    public Task getEntity() {
        return sendEmailAuthenticationTaskEntity;
    }

    @Override
    public void doTask() {
        final var emailData = objectMapper.convertValue(sendEmailAuthenticationTaskEntity.getPayload(), EmailData.class);
        final var guest = emailData.guest();
        final var subscriber = SubscriberPayloadDto.builder()
                .subscriberId(String.valueOf(guest.getId()))
                .firstName(guest.getFirstname())
                .lastName(guest.getLastname())
                .email(guest.getEmail())
                .phone(guest.getPhoneNumber())
                .build();
        final var to = TriggerEventRequestDtoTo2.of(subscriber);
        final var payload = Map.of("targetId", (Object) sendEmailAuthenticationTaskEntity.getTargetId());
        final var workflow = NovuWorkflow.TEST_WORKFLOW.getWorkflowBuilder().to(to).payload(payload).build();
        //TOOD configure payload
        novu.trigger().body(workflow).call();
    }
}
