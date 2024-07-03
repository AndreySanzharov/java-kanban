import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalTests {
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
        Assertions.assertNotEquals(InMemoryHistoryManager.history.getFirst().getClass(),
                InMemoryHistoryManager.history.getLast().getClass(), "Типы задач равны!");
    }

    @Test
    public void TheTaskIsUnchangedWhenAddedToTheManager() {
        inMemoryTaskManager.addTask(task);
        Assertions.assertEquals(task, inMemoryTaskManager.taskMap.get(0));
    }

    @Test
    public void OldTasksStayInHistory() {
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.getTaskById(0);
        Task task2 = new Task("Обновленная задача", "Взять меня в Яндекс по доброте душевной :)",
                Status.DONE);
        inMemoryTaskManager.addTask(task2);

        inMemoryTaskManager.updateTask(0, task2);
        inMemoryTaskManager.getTaskById(0);
        System.out.println(InMemoryHistoryManager.history);
        Assertions.assertTrue(InMemoryHistoryManager.history.size() > 1);
    }
}
