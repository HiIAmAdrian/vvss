package tasks.model;

import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Task Class Tests")
class TaskTest {

    private Task task;

    @BeforeAll
    static void init() {
        System.out.println("Starting Task tests...");
    }

    @BeforeEach
    void setUp() {
        try {
            task = new Task("Meeting", Task.getDateFormat().parse("2023-04-26 14:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Valid Task with Single Time")
    void testValidTaskWithSingleTime() throws ParseException {
        assertEquals("Meeting", task.getTitle());
        assertEquals("2023-04-26 14:00", task.getFormattedDateStart());
    }

    @Test
    @DisplayName("Invalid Task with Negative Time (ECP)")
    void testInvalidTaskWithNegativeTime() throws ParseException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task("Deadline", Task.getDateFormat().parse("1969-12-31 23:59"));
        });
        assertTrue(exception.getMessage().contains("Time cannot be negative"));
    }

    @Test
    @DisplayName("Valid Task with Start, End, and Interval (ECP)")
    void testValidTaskWithStartEndAndInterval() throws ParseException {
        Date start = Task.getDateFormat().parse("2023-04-26 14:00");
        Date end = Task.getDateFormat().parse("2023-04-26 16:00");
        int interval = 60;
        Task task = new Task("Study", start, end, interval);
        assertEquals("Study", task.getTitle());
        assertEquals(start, task.getStartTime());
        assertEquals(end, task.getEndTime());
        assertEquals(interval, task.getRepeatInterval());
    }

    @Test
    @DisplayName("Invalid Task with Negative Interval (ECP)")
    void testInvalidTaskWithNegativeInterval() throws ParseException {
        Date start = Task.getDateFormat().parse("2023-04-26 14:00");
        Date end = Task.getDateFormat().parse("2023-04-26 16:00");
        int interval = -5;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task("Negative Interval", start, end, interval);
        });
        assertTrue(exception.getMessage().contains("interval should me > 1"));
    }

    @Test
    @DisplayName("Valid Task at Boundary Time (BVA)")
    void testValidTaskWithBoundaryTime() throws ParseException {
        Task task = new Task("MinValidTime", Task.getDateFormat().parse("1970-01-01 02:00"));
        assertEquals("1970-01-01 02:00", task.getFormattedDateStart());
    }

    @Test
    @DisplayName("Invalid Task with Negative Boundary Time (BVA)")
    void testInvalidTaskWithNegativeBoundaryTime() throws ParseException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task("NegativeTime", Task.getDateFormat().parse("1969-12-31 23:59"));
        });
        assertTrue(exception.getMessage().contains("Time cannot be negative"));
    }

    @Test
    @DisplayName("Valid Task with Minimum Valid Interval (BVA)")
    void testValidTaskWithMinValidInterval() throws ParseException {
        Date start = Task.getDateFormat().parse("2023-04-26 14:00");
        Date end = Task.getDateFormat().parse("2023-04-26 16:00");
        int interval = 1;
        Task task = new Task("MinValidInterval", start, end, interval);
        assertEquals(interval, task.getRepeatInterval());
    }

    @Test
    @DisplayName("Invalid Task with Zero Interval (BVA)")
    void testInvalidTaskWithZeroInterval() throws ParseException {
        Date start = Task.getDateFormat().parse("2023-04-26 14:00");
        Date end = Task.getDateFormat().parse("2023-04-26 16:00");
        int interval = 0;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task("ZeroInterval", start, end, interval);
        });
        assertTrue(exception.getMessage().contains("interval should me > 1"));
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test completed.");
    }

    @AfterAll
    static void cleanup() {
        System.out.println("All Task tests finished.");
    }
}