package tasks.integration;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceRepoIntegrationTest {

    @Test
    public void testObservableListIntegrationWithMockTask() {
        ArrayTaskList repository = new ArrayTaskList();
        TasksService service = new TasksService(repository);

        Task mockTask = mock(Task.class);
        when(mockTask.getTitle()).thenReturn("Mock Task");

        repository.add(mockTask);  // Interact directly with R

        ObservableList<Task> tasks = service.getObservableList();

        assertEquals(1, tasks.size());
        assertEquals("Mock Task", tasks.get(0).getTitle());
    }

    @Test
    public void testFilterTasksIntegrationWithMockTask() {
        ArrayTaskList repository = new ArrayTaskList();
        TasksService service = new TasksService(repository);

        Date now = new Date();
        Date future = new Date(now.getTime() + 10000);

        Task mockTask = mock(Task.class);
        when(mockTask.isActive()).thenReturn(true);
        when(mockTask.isRepeated()).thenReturn(false);
        when(mockTask.getTime()).thenReturn(new Date(now.getTime() + 5000));
        when(mockTask.nextTimeAfter(any())).thenAnswer(invocation -> {
            Date input = invocation.getArgument(0);
            Date taskTime = new Date(now.getTime() + 5000);
            return input.before(taskTime) ? taskTime : null;
        });

        repository.add(mockTask);  // Add mock task directly to R

        Iterable<Task> result = service.filterTasks(now, future);
        assertTrue(result.iterator().hasNext());
    }
}
