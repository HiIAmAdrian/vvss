package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisplayTaskTest {

    @Test
    void testIncoming_F01_TC02() {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        TasksOperations tasksOperations = new TasksOperations(tasks);

        Iterable<Task> incomingTasks = tasksOperations.incoming(null, null);

        List<Task> result = new ArrayList<>();
        incomingTasks.forEach(result::add);

        assertEquals(0, result.size());
    }

    @Test
    void testIncoming_F02_TC03() {
        ObservableList<Task> tasks = FXCollections.observableArrayList(
                new Task("Task1", new Date(80, 4, 15, 12, 30), new Date(80, 4, 15, 12, 30), 1),
                new Task("Task2", new Date(80, 4, 20, 12, 30), new Date(80, 4, 20, 12, 30), 1),
                new Task("Task3", new Date(80, 4, 25, 12, 30), new Date(80, 4, 25, 12, 30), 1),
                new Task("Task4", new Date(80, 4, 30, 12, 30), new Date(80, 4, 30, 12, 30), 1)
        );
        tasks.forEach(task -> task.setActive(true));
        TasksOperations tasksOperations = new TasksOperations(tasks);

        Iterable<Task> incomingTasks = tasksOperations.incoming(
                new Date(80, 4, 17, 12, 30),
                new Date(80, 4, 26, 12, 30)
        );

        List<Task> result = new ArrayList<>();
        incomingTasks.forEach(result::add);

        assertEquals(2, result.size());
        assertEquals("Task2", result.get(0).getTitle());
        assertEquals("Task3", result.get(1).getTitle());
    }

    @Test
    void testIncoming_F02_TC04() {
        ObservableList<Task> tasks = FXCollections.observableArrayList(
                new Task("Task1", new Date(80, 4, 15, 12, 30), new Date(80, 4, 15, 12, 30), 1),
                new Task("Task2", new Date(80, 4, 20, 12, 30), new Date(80, 4, 20, 12, 30), 1),
                new Task("Task3", new Date(80, 4, 25, 12, 30), new Date(80, 4, 25, 12, 30), 1),
                new Task("Task4", new Date(80, 4, 30, 12, 30), new Date(80, 4, 30, 12, 30), 1)
        );
        tasks.forEach(task -> task.setActive(true));

        TasksOperations tasksOperations = new TasksOperations(tasks);

        Iterable<Task> incomingTasks = tasksOperations.incoming(
                new Date(80, 4, 21, 12, 30),
                new Date(80, 4, 22, 12, 30)
        );

        List<Task> result = new ArrayList<>();
        incomingTasks.forEach(result::add);

        assertEquals(0, result.size());
    }

    @Test
    void testIncoming_F02_TC05() {
        ObservableList<Task> tasks = FXCollections.observableArrayList(
                new Task("Task1", new Date(80, 4, 15, 12, 30), new Date(80, 4, 15, 12, 30), 1),
                new Task("Task2", new Date(80, 4, 20, 12, 30), new Date(80, 4, 20, 12, 30), 1),
                new Task("Task3", new Date(80, 4, 25, 12, 30), new Date(80, 4, 25, 12, 30), 1),
                new Task("Task4", new Date(80, 4, 30, 12, 30), new Date(80, 4, 30, 12, 30), 1)
        );
        tasks.forEach(task -> task.setActive(true));

        TasksOperations tasksOperations = new TasksOperations(tasks);

        Iterable<Task> incomingTasks = tasksOperations.incoming(
                null,
                new Date(80, 4, 22, 12, 30)
        );

        List<Task> result = new ArrayList<>();
        incomingTasks.forEach(result::add);

        assertEquals(0, result.size());
    }

    @Test
    void testIncoming_F02_TC06() {
        ObservableList<Task> tasks = FXCollections.observableArrayList(
                new Task("Task1", new Date(80, 4, 15, 12, 30), new Date(80, 4, 15, 12, 30), 1),
                new Task("Task2", new Date(80, 4, 20, 12, 30), new Date(80, 4, 20, 12, 30), 1),
                new Task("Task3", new Date(80, 4, 25, 12, 30), new Date(80, 4, 25, 12, 30), 1),
                new Task("Task4", new Date(80, 4, 30, 12, 30), new Date(80, 4, 30, 12, 30), 1)
        );
        tasks.forEach(task -> task.setActive(true));

        TasksOperations tasksOperations = new TasksOperations(tasks);

        Iterable<Task> incomingTasks = tasksOperations.incoming(
                new Date(80, 4, 22, 12, 30),
                null
        );

        List<Task> result = new ArrayList<>();
        incomingTasks.forEach(result::add);

        assertEquals(0, result.size());
    }

    @Test
    void testIncoming_F02_TC07() {
        ObservableList<Task> tasks = FXCollections.observableArrayList(
                new Task("Task1", new Date(80, 4, 20, 12, 30), new Date(80, 4, 20, 12, 30), 1)
        );
        tasks.forEach(task -> task.setActive(true));

        TasksOperations tasksOperations = new TasksOperations(tasks);

        Iterable<Task> incomingTasks = tasksOperations.incoming(
                new Date(80, 4, 17, 12, 30),
                new Date(80, 4, 26, 12, 30)
        );

        List<Task> result = new ArrayList<>();
        incomingTasks.forEach(result::add);

        assertEquals(1, result.size());
        assertEquals("Task1", result.get(0).getTitle());
    }

    @Test
    void testIncoming_F02_TC08() {
        ObservableList<Task> tasks = FXCollections.observableArrayList(
                new Task("Task1", new Date(80, 4, 20, 12, 30), new Date(80, 4, 20, 12, 30), 1),
                new Task("Task2", new Date(80, 4, 21, 12, 30), new Date(80, 4, 21, 12, 30), 1)
        );
        tasks.forEach(task -> task.setActive(true));

        TasksOperations tasksOperations = new TasksOperations(tasks);

        Iterable<Task> incomingTasks = tasksOperations.incoming(
                new Date(80, 4, 17, 12, 30),
                new Date(80, 4, 26, 12, 30)
        );

        List<Task> result = new ArrayList<>();
        incomingTasks.forEach(result::add);

        assertEquals(2, result.size());
        assertEquals("Task1", result.get(0).getTitle());
        assertEquals("Task2", result.get(1).getTitle());
    }
}