package tasks.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskTestLab04 {

    @Test
    public void testConstructorAndGetters() {
        Date time = new Date();
        Task task = new Task("Test task", time);

        assertEquals("Test task", task.getTitle());
        assertEquals(time, task.getTime());
    }

    @Test
    public void testSetActive() {
        Task task = new Task("Inactive task", new Date());
        assertFalse(task.isActive());

        task.setActive(true);
        assertTrue(task.isActive());
    }
}