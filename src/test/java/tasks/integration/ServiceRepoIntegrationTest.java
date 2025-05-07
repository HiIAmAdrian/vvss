package tasks.integration;

import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceRepoIntegrationTest {

    @Test
    public void testAddMockTaskToRepoThroughService() {
        ArrayTaskList repo = new ArrayTaskList();
        TasksService service = new TasksService(repo);

        Task mockTask = mock(Task.class);
        when(mockTask.getTitle()).thenReturn("Mock Task");
        when(mockTask.getTime()).thenReturn(new Date());
        when(mockTask.isActive()).thenReturn(true);

        service.getObservableList().add(mockTask);

        assertEquals(1, repo.size());
        assertEquals("Mock Task", repo.getTask(0).getTitle());
    }

    @Test
    public void testServiceCallsRepoIterator() {
        ArrayTaskList spyRepo = spy(new ArrayTaskList());
        TasksService service = new TasksService(spyRepo);

        service.getObservableList(); // trigger internal call to repo.iterator()

        verify(spyRepo, atLeastOnce()).iterator();
    }
}
