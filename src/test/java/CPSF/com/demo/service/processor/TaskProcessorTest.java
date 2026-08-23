package CPSF.com.demo.service.processor;

import CPSF.com.demo.model.constant.TaskStatus;
import CPSF.com.demo.model.entity.Task;
import CPSF.com.demo.service.core.SearchCriteria;
import CPSF.com.demo.service.processor.task.ExecutableTask;
import CPSF.com.demo.service.processor.task.ExecutableTaskFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskProcessorTest {

    @Mock
    private TaskService taskService;

    @Mock
    private ExecutableTaskFactory executableTaskFactory;

    @Mock
    private ExecutableTask executableTask;

    @InjectMocks
    private TaskProcessor taskProcessor;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    @Captor
    private ArgumentCaptor<List<Task>> taskListCaptor;

    @Test
    void shouldDoNothingWhenNoPendingTasks() {
        // given
        when(taskService.findBy(any(SearchCriteria[].class))).thenReturn(new PageImpl(List.of()));

        // when
        taskProcessor.processTask();

        // then
        verify(taskService, never()).update(any(Task.class));
        verify(taskService, never()).update(anyList());
        verifyNoInteractions(executableTaskFactory);
    }

    @Test
    void shouldExecuteFlatTasksSuccessfully() throws Exception {
        // given
        final var task1 = new Task(1, null);
        task1.setTaskStatus(TaskStatus.PENDING);

        final var task2 = new Task(2, null);
        task2.setTaskStatus(TaskStatus.PENDING);

        when(taskService.findBy(any(SearchCriteria[].class))).thenReturn(new PageImpl(List.of(task1, task2)));
        when(executableTaskFactory.getExecutableTask(any())).thenReturn(executableTask);

        // when
        taskProcessor.processTask();

        // then
        verify(executableTask, times(2)).doTask();

        verify(taskService, times(4)).update(taskCaptor.capture());

        assertThat(task1.getTaskStatus()).isEqualTo(TaskStatus.EXECUTED);
        assertThat(task2.getTaskStatus()).isEqualTo(TaskStatus.EXECUTED);
    }

    @Test
    void shouldExecuteTaskTreeSuccessfully() throws Exception {
        // given
        final var rootTask = new Task(1, null);

        final var subTask = new Task(2, rootTask);

        final var grandChildTask = new Task(3, subTask);

        when(taskService.findBy(any(SearchCriteria[].class))).thenReturn(new PageImpl(List.of(rootTask, subTask, grandChildTask)));
        when(executableTaskFactory.getExecutableTask(any())).thenReturn(executableTask);

        // when
        taskProcessor.processTask();

        // then
        verify(executableTask, times(3)).doTask();

        verify(taskService, times(6)).update(taskCaptor.capture());

        assertThat(rootTask.getTaskStatus()).isEqualTo(TaskStatus.EXECUTED);
        assertThat(subTask.getTaskStatus()).isEqualTo(TaskStatus.EXECUTED);
        assertThat(grandChildTask.getTaskStatus()).isEqualTo(TaskStatus.EXECUTED);
    }

    @Test
    void shouldFailTaskAndMarkDescendantsAsFailedWhenExceptionThrown() throws Exception {
        // given
        final var rootTask = new Task(1, null);
        rootTask.setTaskStatus(TaskStatus.PENDING);

        final var subTask = new Task(2, rootTask);
        subTask.setTaskStatus(TaskStatus.PENDING);

        when(taskService.findBy(any(SearchCriteria[].class))).thenReturn(new PageImpl(List.of(rootTask, subTask)));

        when(executableTaskFactory.getExecutableTask(rootTask)).thenThrow(new RuntimeException("Simulated business logic error"));

        // when
        taskProcessor.processTask();

        // then
        verify(executableTaskFactory, never()).getExecutableTask(subTask);

        verify(taskService, atLeastOnce()).update(taskCaptor.capture());

        final var updatedTasks = taskCaptor.getAllValues();

        assertThat(rootTask.getTaskStatus()).isEqualTo(TaskStatus.FAILED);
        assertThat(subTask.getTaskStatus()).isEqualTo(TaskStatus.FAILED);
    }

    @Test
    void shouldIncrementRetryCountForPreviouslyFailedTasks() throws Exception {
        // given
        final var failedTask = new Task(1, null);
        failedTask.setTaskStatus(TaskStatus.FAILED);
        failedTask.setRetryCount(1);

        when(taskService.findBy(any(SearchCriteria[].class))).thenReturn(new PageImpl(List.of(failedTask)));
        when(executableTaskFactory.getExecutableTask(any())).thenReturn(executableTask);

        // when
        taskProcessor.processTask();

        // then
        assertThat(failedTask.getRetryCount()).isEqualTo(2);
        verify(executableTask, times(1)).doTask();
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldFailAllInProgressTasksWhenGlobalExceptionOccurs() {
        // given
        final var pendingTask = new Task(1, null);
        pendingTask.setTaskStatus(TaskStatus.PENDING);

        final var inProgressTask = new Task(2, null);
        inProgressTask.setTaskStatus(TaskStatus.IN_PROGRESS);

        when(taskService.findBy(any(SearchCriteria[].class)))
                .thenReturn(new PageImpl(List.of(pendingTask)))
                .thenReturn(new PageImpl(List.of(inProgressTask)));

        try (MockedStatic<Executors> executorsMock = mockStatic(Executors.class)) {
            executorsMock.when(Executors::newVirtualThreadPerTaskExecutor)
                    .thenThrow(new RuntimeException("Simulated runtime failure creating executor"));

            // when
            taskProcessor.processTask();
        }

        // then
        verify(taskService).update(taskListCaptor.capture());

        final var failedList = (List<Task>) taskListCaptor.getValue();
        assertThat(failedList.size()).isEqualTo(1);
        assertThat(failedList.getFirst().getId()).isEqualTo(2L);
        assertThat(failedList.getFirst().getTaskStatus()).isEqualTo(TaskStatus.FAILED);
    }
}