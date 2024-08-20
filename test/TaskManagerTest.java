import javakanban.elements.Epic;
import javakanban.elements.Status;
import javakanban.elements.Subtask;
import javakanban.elements.Task;
import javakanban.managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @BeforeEach
    void createTasks() {
        Task task = new Task("AAA", "AAA", Status.NEW,
                Duration.ofMinutes(55), LocalDateTime.of(2024, 11, 10, 10, 0));
        Epic epic = new Epic("BBB", "BBB");
        Subtask subtask1 = new Subtask("CCC", "CCC", Status.NEW,
                Duration.ofMinutes(55),  LocalDateTime.of(2024, 11, 12, 11, 0));
        Subtask subtask2 = new Subtask("DDD", "DDD", Status.NEW,
                Duration.ofMinutes(55),  LocalDateTime.of(2024, 11, 13, 11, 0));
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addEpic(epic);

    }

    @Test
    void getAll() {
        assertTrue(!inMemoryTaskManager.getAll().isEmpty());
    }

    @Test
    void deleteAll() {
        inMemoryTaskManager.deleteAll();
        System.out.println(inMemoryTaskManager.taskMap);
        System.out.println(inMemoryTaskManager.epicMap);
        assertTrue((inMemoryTaskManager.taskMap.isEmpty()) & (inMemoryTaskManager.epicMap.isEmpty()));
    }

    @Test
    void getTaskById() {
        ArrayList<Task> mustBeMoreThatZero = new ArrayList<>();
        mustBeMoreThatZero.add(inMemoryTaskManager.getTaskById(0));
        assertTrue(!mustBeMoreThatZero.isEmpty());
    }

    @Test
    void updateTask() {
        Task oldTask = inMemoryTaskManager.taskMap.get(0);
        System.out.println(oldTask);
        Task newTask = new Task("KKK", "KKK", Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(0, newTask);
        assertNotEquals(oldTask, newTask);
    }

    @Test
    void getEpicById() {
        List<Task> mustBeMoreThanZero = new ArrayList<>(1);
        mustBeMoreThanZero.add(inMemoryTaskManager.getEpicById(1));
        assertTrue(!mustBeMoreThanZero.isEmpty());

    }

    @Test
    void addSubtask() {
        inMemoryTaskManager.addSubtask(1, new Subtask("VVV", "VVV", Status.DONE,
                Duration.ofMinutes(55),  LocalDateTime.of(2024, 11, 12, 11, 0)));
        Epic epic = inMemoryTaskManager.epicMap.get(1);
        System.out.println(epic.getSubtaskList());
        assertTrue(!epic.getSubtaskList().isEmpty());
    }

    @Test
    void updateSubtask() {
        Subtask oldSub = new Subtask("PPP", "PPP", Status.IN_PROGRESS,
                Duration.ofMinutes(55),  LocalDateTime.of(2024, 11, 12, 11, 0));
        inMemoryTaskManager.addSubtask(1, oldSub);
        Subtask newSub = new Subtask("KKK", "KKK", Status.DONE,
                Duration.ofMinutes(55),  LocalDateTime.of(2024, 11, 12, 11, 0));
        inMemoryTaskManager.updateSubtask(1, 0, newSub);

        System.out.println(inMemoryTaskManager.epicMap);

        Epic oldEpic = inMemoryTaskManager.epicMap.get(1);
        System.out.println(oldEpic.getSubtaskList());
    }

    @Test
    void deleteTaskById() {
        assertTrue(!inMemoryTaskManager.taskMap.isEmpty(), "Список и так пустой");
        System.out.println(inMemoryTaskManager.taskMap);
        inMemoryTaskManager.deleteTaskById(0);
        assertTrue(inMemoryTaskManager.taskMap.isEmpty());
    }

}