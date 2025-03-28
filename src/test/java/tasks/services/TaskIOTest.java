package tasks.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tasks.model.LinkedTaskList;
import tasks.model.Task;
import tasks.model.TaskList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("TaskIO Add Method Tests")
class TaskIOTest {

    private TaskList taskList;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setUp() {
        taskList = new LinkedTaskList();
    }

    // ECP - Valid Test 1: Adding a single valid task
    @Test
    @DisplayName("ECP - Add Valid Task")
    void testAddValidTask() throws ParseException {
        Task task = new Task("Meeting", dateFormat.parse("2025-05-10 12:00"), dateFormat.parse("2025-05-10 15:00"),1);
        taskList.add(task);
        assertEquals(1, taskList.size());
    }

    // ECP - Valid Test 2: Adding multiple valid tasks
    @Test
    @DisplayName("ECP - Add Multiple Valid Tasks")
    void testAddMultipleValidTasks() throws ParseException {
        Task task1 = new Task("Workout", dateFormat.parse("2025-06-01 08:00"));
        Task task2 = new Task("Lunch", dateFormat.parse("2025-06-01 13:00"));
        taskList.add(task1);
        taskList.add(task2);
        assertEquals(2, taskList.size());
    }

    // ECP - Invalid Test 1: Adding a null task
    @Test
    @DisplayName("ECP - Add Task with Invalid Date (Before 1970)")
    void testAddTaskWithInvalidDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Task("Old Task", dateFormat.parse("1969-12-31 23:59"))
        );
        assertNotNull(exception);
        assertEquals("Time cannot be negative: -7260000", exception.getMessage());
    }

    // ECP - Invalid Test 2: Adding a task with an empty title
    @Test
    @DisplayName("ECP - Add Task with Zero Interval")
    void testAddTaskWithZeroIntervalECP() throws ParseException {
        Date start = dateFormat.parse("2025-07-01 10:00");
        Date end = dateFormat.parse("2025-07-01 12:00");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Task("Zero Interval Task", start, end, 0)
        );
        assertNotNull(exception);
        assertEquals("interval should me > 1", exception.getMessage());
    }


    // BVA - Valid Test 1: Adding a task at the earliest valid date
    @Test
    @DisplayName("BVA - Add Task at Earliest Valid Date")
    void testAddTaskAtEarliestValidDate() throws ParseException {
        Task task = new Task("Epoch Task", dateFormat.parse("1970-01-01 02:00"));
        taskList.add(task);
        assertEquals(1, taskList.size());
    }

    // BVA - Valid Test 2: Adding a task at a maximum reasonable date
    @Test
    @DisplayName("BVA - Add Task at Future Date")
    void testAddTaskAtFutureDate() throws ParseException {
        Task task = new Task("Future Task", dateFormat.parse("2100-12-31 23:59"));
        taskList.add(task);
        assertEquals(1, taskList.size());
    }

    // BVA - Invalid Test 1: Adding a task with a past invalid date
    @Test
    @DisplayName("BVA - Add Task with Negative Date")
    void testAddTaskWithNegativeDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Task("Past Task", new Date(-1)));
        assertTrue(exception.getMessage().contains("Time cannot be negative"));
    }

    // BVA - Invalid Test 2: Adding a task with an interval of zero
    @Test
    @DisplayName("BVA - Add Task with Zero Interval")
    void testAddTaskWithZeroInterval() throws ParseException {
        Date start = dateFormat.parse("2025-08-10 09:00");
        Date end = dateFormat.parse("2025-08-10 10:00");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Task("Zero Interval Task", start, end, 0));
        assertTrue(exception.getMessage().contains("interval should me > 1"));
    }

}