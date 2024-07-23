import JavaKanban.elements.Epic;
import JavaKanban.elements.Status;
import JavaKanban.elements.Task;
import JavaKanban.managers.InMemoryHistoryManager;
import JavaKanban.managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExeptionsTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    Task task = new Task("AAA", "BBB", Status.NEW);
    Epic epic = new Epic("AAA", "BBB");

    @Test
    public void tasksAreEqualIfTheyIdAreEqual() {
        inMemoryTaskManager.addTask(task);
        Task createdTask = inMemoryTaskManager.getTaskById(0);
        Assertions.assertEquals(task, createdTask, "Задачи не равны при равном ID");
    }

    @Test
    public void epicsAreEqualIfTheIdAreEqual() {
        inMemoryTaskManager.addEpic(epic);
        Epic createdEpic = inMemoryTaskManager.getEpicById(0);
        Assertions.assertEquals(epic, createdEpic, "Эпики не равны при равном ID");
    }

    @Test
    public void typesOfHistoryIsDifferent() {
        inMemoryTaskManager.addTask(task);
        System.out.println(task);

        Epic epic = new Epic("Эпик", "Эпик делится на подзадачи");
        inMemoryTaskManager.addEpic(epic);
        System.out.println(epic);

        inMemoryTaskManager.getTaskById(0);
        inMemoryTaskManager.getEpicById(1);

        Assertions.assertNotNull(InMemoryHistoryManager.history, "Список истории пуст!");
    }

    @Test
    public void TheTaskIsUnchangedWhenAddedToTheManager() {
        inMemoryTaskManager.addTask(task);
        Assertions.assertEquals(task, inMemoryTaskManager.taskMap.get(0));
    }
}
