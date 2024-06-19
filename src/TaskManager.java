import java.util.HashMap;

public class TaskManager {
    int nextId = 1;

    HashMap<Integer, Task> taskMap = new HashMap<>();


    public HashMap<Integer, Task> getAll() {
        return taskMap;
    }

    public void deleteAll() {
        taskMap.clear();
    }

    public Object getFromId(int id) {
        return taskMap.get(id);
    }

    public void add(Task task) {
        task.id = nextId;
        nextId++;
        taskMap.put(task.id, task);

    }

    public void add(Epic epic) {
        epic.id = nextId;
        nextId++;
        taskMap.put(epic.id, epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.id = nextId;
        nextId++;
    }

    // по ТЗ обновление задачи это передча новой задачи и "верного идентификатора". Получается, id мы будем передавать.
    public void updateTask(int id, Task task) {
        taskMap.put(id, task);
    }

    public void deleteFromId(int id) {
        taskMap.remove(id);
    }

}
