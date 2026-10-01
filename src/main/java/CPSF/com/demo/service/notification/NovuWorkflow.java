package CPSF.com.demo.service.notification;

import co.novu.models.components.TriggerEventRequestDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NovuWorkflow {

    VERIFY_RESERVATION_WORKFLOW("reservation-verification-email");

    private final String workflow;

    public TriggerEventRequestDto.Builder getWorkflowBuilder() {
        return TriggerEventRequestDto.builder().workflowId(workflow);
    }

}
