import javakanban.elements.Status;
import javakanban.elements.Task;
import javakanban.managers.InMemoryHistoryManager;
import javakanban.managers.InMemoryTaskManager;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class HistoryManagerTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();


    @BeforeEach
    public void create() {
        Task task = new Task("Задача", "Задача неделима", Status.NEW,
                Duration.ofMinutes(55), LocalDateTime.of(2024, 11, 10, 10, 0));
        inMemoryTaskManager.addTask(task);
    }


    @Test
    public void theSameTasksAreSavedInHistoryOnlyOnce() {
        inMemoryTaskManager.getTaskById(0);
        inMemoryTaskManager.getTaskById(0);
        Assertions.assertEquals(2, InMemoryHistoryManager.history.size());
    }

    @Test
    public void removeHistoryById() {
        inMemoryTaskManager.getTaskById(0);
        inMemoryHistoryManager.remove(0);
        Assertions.assertEquals(1, InMemoryHistoryManager.history.size());
    }

}
