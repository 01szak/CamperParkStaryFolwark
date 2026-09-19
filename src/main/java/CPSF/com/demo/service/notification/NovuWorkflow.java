package CPSF.com.demo.service.notification;

import co.novu.models.components.TriggerEventRequestDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NovuWorkflow {

    TEST_WORKFLOW("test-workflow");

    private final String workflow;

    public TriggerEventRequestDto.Builder getWorkflowBuilder() {
        return TriggerEventRequestDto.builder().workflowId(workflow);
    }

}
