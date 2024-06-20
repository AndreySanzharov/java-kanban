import java.util.HashMap;

public class TaskManager {
    int id = 1;

    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();


    public HashMap<Integer, Task> getAll() {
        return taskMap;
    }

    public void deleteAll() {
        taskMap.clear();
        epicMap.clear();
    }

    public Object getTaskById(int id) {

        return taskMap.get(id);
    }

    public Object getEpicById(int id) {

        return epicMap.get(id);
    }


    public void addTask(Task task) {
        id++;
        taskMap.put(task.id, task);
    }

    public void addEpic(Epic epic) {
        id++;
        epicMap.put(epic.id, epic);
    }

    public void addSubtask(int id, Subtask subtask) {
        Epic epic = epicMap.get(id);
        epic.addSubtask(subtask);
    }


    public void updateTask(int id, Task task) {
        taskMap.put(id, task);
    }

    public void updateSubtask(int epicId, int subId, Subtask subtask) {
        Epic epic = epicMap.get(epicId);
        epic.updateSubtask(subId, subtask);
    }

    public void deleteSubtask(int epicId, int subId) {
        Epic epic = epicMap.get(epicId);
        epic.deleteSubtask(subId);
    }

    public void deleteTaskById(int id) {
        taskMap.remove(id);
    }

    public void deleteEpicById(int id) {
        epicMap.remove(id);
    }

}
