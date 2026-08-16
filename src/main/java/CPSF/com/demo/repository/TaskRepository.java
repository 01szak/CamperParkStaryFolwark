package CPSF.com.demo.repository;

import CPSF.com.demo.model.entity.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CRUDRepository<Task> {
}
