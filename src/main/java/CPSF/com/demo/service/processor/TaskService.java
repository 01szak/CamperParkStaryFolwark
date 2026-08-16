package CPSF.com.demo.service.processor;

import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.repository.TaskRepository;
import CPSF.com.demo.service.core.CRUDServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService extends CRUDServiceImpl {

    private final TaskRepository taskRepository;

    @Override
    protected CRUDRepository getRepository() {
        return taskRepository;
    }
}
