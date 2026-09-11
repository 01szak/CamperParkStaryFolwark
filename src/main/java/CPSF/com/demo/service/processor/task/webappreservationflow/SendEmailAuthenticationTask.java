package CPSF.com.demo.service.processor.task.webappreservationflow;

import CPSF.com.demo.model.EmailData;
import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.notification.NovuWorkflow;
import CPSF.com.demo.service.processor.task.ExecutableTask;
import co.novu.Novu;
import co.novu.models.components.SubscriberPayloadDto;
import co.novu.models.components.TriggerEventRequestDtoTo2;
import lombok.RequiredArgsConstructor;

import java.util.Map;


@RequiredArgsConstructor
public class SendEmailAuthenticationTask implements ExecutableTask {

    private final Novu novu;
    private final Task sendEmailAuthenticationTaskEntity;

    @Override
    public void doTask() {
        final var emailData = objectMapper.convertValue(sendEmailAuthenticationTaskEntity.getPayload(), EmailData.class);
        final var guest = emailData.guest();
        final var additionalPayload = emailData.additionalPayload();
        final var subscriber = buildSubscriber(guest);
        final var to = TriggerEventRequestDtoTo2.of(subscriber);
        final var payload = Map.of(
                "targetId", sendEmailAuthenticationTaskEntity.getTargetId(),
                "additionalPayload", additionalPayload
        );
        final var workflow = NovuWorkflow.TEST_WORKFLOW.getWorkflowBuilder().to(to).payload(payload).build();
        novu.trigger().body(workflow).call();
    }

    private SubscriberPayloadDto buildSubscriber(GuestDTO guest) {
        return SubscriberPayloadDto.builder()
                .subscriberId(String.valueOf(guest.id()))
                .firstName(guest.firstname())
                .lastName(guest.lastname())
                .email(guest.email())
                .phone(guest.phoneNumber())
                .build();
    }

}
