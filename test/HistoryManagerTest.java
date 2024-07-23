import javakanban.elements.Status;
import javakanban.elements.Task;
import javakanban.managers.InMemoryHistoryManager;
import javakanban.managers.InMemoryTaskManager;
import org.junit.jupiter.api.*;

public class HistoryManagerTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();


    @BeforeEach
    public void create(){
        Task task = new Task("Задача", "Задача неделима", Status.NEW);
        inMemoryTaskManager.addTask(task);
    }


    @Test
    public void theSameTasksAreSavedInHistoryOnlyOnce(){
        inMemoryTaskManager.getTaskById(0);
        inMemoryTaskManager.getTaskById(0);

        System.out.println(InMemoryHistoryManager.history);

        Assertions.assertTrue(InMemoryHistoryManager.history.size() == 1);
    }

    @Test
    public void removeHistoryById(){
        inMemoryTaskManager.getTaskById(0);
        inMemoryHistoryManager.remove(0);
        Assertions.assertEquals(0, InMemoryHistoryManager.history.size());

    }

}
