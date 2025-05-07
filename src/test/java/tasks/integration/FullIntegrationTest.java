package tasks.integration;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.Date;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class FullIntegrationTest {

    @Test
    public void testAddAndRetrieveTaskThroughService() {
        ArrayTaskList repo = new ArrayTaskList();
        TasksService service = new TasksService(repo);

        Task task = new Task("Real Task", new Date(2025 - 1900, 4, 1, 10, 0));
        task.setActive(true);
        repo.add(task);
        ObservableList<Task> tasks = service.getObservableList();

        assertEquals(1, tasks.size());
        assertEquals("Real Task", repo.getTask(0).getTitle());
    }

    @Test
    public void testServiceFiltersIncomingTasks() {
        ArrayTaskList repo = new ArrayTaskList();
        TasksService service = new TasksService(repo);

        Task t1 = new Task("T1", new Date(2025 - 1900, 4, 1, 10, 0));
        t1.setActive(true);
        Task t2 = new Task("T2", new Date(2025 - 1900, 6, 1, 10, 0));
        t2.setActive(true);

        repo.add(t1);
        repo.add(t2);

        Iterable<Task> result = service.filterTasks(
                new Date(2025 - 1900, 3, 30, 0, 0),
                new Date(2025 - 1900, 4, 2, 0, 0)
        );

        long count = StreamSupport.stream(result.spliterator(), false).count();
        assertEquals(1, count);
    }
}

