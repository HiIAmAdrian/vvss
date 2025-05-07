package tasks.services;

import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.util.Date;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TasksServiceTest {
    @Test
    public void testGetObservableList() {
        ArrayTaskList mockedList = mock(ArrayTaskList.class);
        TasksService service = new TasksService(mockedList);

        assertNotNull(service.getObservableList());
        verify(mockedList, atLeastOnce()).iterator();
    }

    @Test
    public void testIncomingTasksFilter() {
        Task task1 = new Task("Task 1", new Date(2025 - 1900, 4, 1, 10, 0));
        task1.setActive(true);

        Task task2 = new Task("Task 2", new Date(2025 - 1900, 5, 1, 10, 0));
        task2.setActive(true);

        ArrayTaskList mockedList = mock(ArrayTaskList.class);
        when(mockedList.iterator()).thenReturn(java.util.Arrays.asList(task1, task2).iterator());

        TasksService service = new TasksService(mockedList);

        Date start = new Date(2025 - 1900, 3, 30, 0, 0);
        Date end = new Date(2025 - 1900, 4, 2, 0, 0);

        Iterable<Task> incoming = service.filterTasks(start, end);

        long count = StreamSupport.stream(incoming.spliterator(), false).count();
        assertEquals(1, count);
    }
}