package tasks.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ArrayTaskListTest {

    private Task task1;
    private Task task2;
    private ArrayTaskList list;

    @BeforeEach
    public void setUp() {
        task1 = new Task("Task 1", new Date(2025 - 1900, 4, 1, 10, 0));
        task2 = new Task("Task 2", new Date(2025 - 1900, 4, 2, 10, 0));
        list = spy(new ArrayTaskList());
    }

    @Test
    public void testAddAndSize() {
        list.add(task1);
        list.add(task2);

        assertEquals(2, list.size());
        verify(list).add(task1);
        verify(list).add(task2);
    }

    @Test
    public void testGetTask() {
        list.add(task1);
        Task result = list.getTask(0);
        assertEquals(task1, result);
    }
}