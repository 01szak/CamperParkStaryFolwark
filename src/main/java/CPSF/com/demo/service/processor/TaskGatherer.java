package CPSF.com.demo.service.processor;

import CPSF.com.demo.model.entity.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Gatherer;

public class TaskGatherer {

    public static Gatherer<Task, Map<Integer, TaskNode>, TaskNode> createTaskForest() {
        return Gatherer.ofSequential(
                HashMap::new,
                (map, task, _) -> {
                    map.put(task.getId(), new TaskNode(task));
                    return true;
                },
                (map, downstream) -> {
                    final var roots = new ArrayList<TaskNode>();

                    map.values().forEach(v -> {
                        final var parent = v.task().getParentTask();

                        Optional.ofNullable(parent).ifPresentOrElse(t -> {
                            if (map.containsKey(t.getId())) {
                                map.get(t.getId()).addSubTask(v);
                            }
                        }, () -> {
                            roots.add(v);
                        });

                    });

                    roots.forEach(downstream::push);
                }
        );
    }
}
