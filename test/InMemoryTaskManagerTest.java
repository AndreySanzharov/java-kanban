import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeEach
    void createTasks() {
        Task task = new Task("AAA", "AAA", Status.NEW);
        Epic epic = new Epic("BBB", "BBB");
        taskManager.addTask(task);
        taskManager.addEpic(epic);
    }

    @Test
    void getAll() {
        System.out.println(taskManager.getAll());
        assertTrue(!taskManager.getAll().isEmpty());
    }

    @Test
    void deleteAll() {
        taskManager.deleteAll();
        System.out.println(taskManager.taskMap);
        System.out.println(taskManager.epicMap);
        assertTrue((taskManager.taskMap.isEmpty()) & (taskManager.epicMap.isEmpty()));
    }

    @Test
    void getTaskById() {
        ArrayList<Task> mustBeMoreThatZero = new ArrayList<>();
        mustBeMoreThatZero.add(taskManager.getTaskById(0));
        assertTrue(!mustBeMoreThatZero.isEmpty());
    }

    @Test
    void updateTask() {
        Task oldTask = taskManager.taskMap.get(0);
        System.out.println(oldTask);
        Task newTask = new Task("KKK", "KKK", Status.IN_PROGRESS);
        taskManager.updateTask(0, newTask);
        System.out.println(taskManager.taskMap.get(0));
        assertNotEquals(oldTask, newTask);
    }

    @Test
    void getEpicById() {
        ArrayList<Epic> mustBeMoreThatZero = new ArrayList<>();
        mustBeMoreThatZero.add(taskManager.getEpicById(0));
        assertTrue(!mustBeMoreThatZero.isEmpty());
    }

    @Test
    void addSubtask() {
        taskManager.addSubtask(1, new Subtask("VVV", "VVV", Status.DONE));
        Epic epic = taskManager.epicMap.get(1);
        System.out.println(epic.subtaskList);
        assertTrue(!epic.subtaskList.isEmpty());
    }

    @Test
    void updateSubtask() {
        Subtask oldSub = new Subtask("PPP", "PPP", Status.IN_PROGRESS);
        taskManager.addSubtask(1, oldSub);
        Subtask newSub = new Subtask("KKK", "KKK", Status.DONE);
        taskManager.updateSubtask(1, 0, newSub);

        System.out.println(taskManager.epicMap);

        Epic oldEpic = taskManager.epicMap.get(1);
        System.out.println(oldEpic.subtaskList);
    }

    @Test
    void deleteTaskById() {
        assertTrue(!taskManager.taskMap.isEmpty(), "Список и так пустой");
        System.out.println(taskManager.taskMap);
        taskManager.deleteTaskById(0);
        assertTrue(taskManager.taskMap.isEmpty());
    }
}