package javakanban.managers;

import javakanban.elements.Epic;
import javakanban.elements.Subtask;
import javakanban.elements.Task;
import javakanban.interfaces.HistoryManager;
import javakanban.interfaces.TaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int id;

    public Map<Integer, Task> taskMap = new HashMap<>();
    public Map<Integer, Epic> epicMap = new HashMap<>();

    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getAll() {
        ArrayList<Task> rez = new ArrayList<>();
        rez.addAll(taskMap.values());
        rez.addAll(epicMap.values());
        return rez;
    }

    @Override
    public void deleteAll() {
        taskMap.clear();
        epicMap.clear();
    }

    @Override
    public Task getTaskById(int id) {
        inMemoryHistoryManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        inMemoryHistoryManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int epId, int subId) {
        Epic epic = epicMap.get(epId);
        inMemoryHistoryManager.add(epic.getSubtaskList().get(subId));
        return epic.getSubtaskList().get(subId);
    }

    @Override
    public void addTask(Task task) {
        task.id = id;
        taskMap.put(task.id, task);
        id++;
    }

    @Override
    public void addEpic(Epic epic) {
        epic.id = id;
        epicMap.put(epic.id, epic);
        id++;
    }

    @Override
    public void addSubtask(int id, Subtask subtask) {
        Epic epic = epicMap.get(id);
        epic.addSubtask(subtask);
        epic.updateStatus();
    }

    @Override
    public void updateTask(int id, Task task) {
        taskMap.put(id, task);
    }

    @Override
    public void updateSubtask(int epicId, int subId, Subtask subtask) {
        Epic epic = epicMap.get(epicId);
        epic.updateSubtask(subId, subtask);
        epic.updateStatus();
    }

    @Override
    public void deleteSubtask(int epicId, int subId) {
        Epic epic = epicMap.get(epicId);
        epic.deleteSubtask(subId);
        epic.updateStatus();
        inMemoryHistoryManager.remove(subId);
    }

    @Override
    public void deleteTaskById(int id) {
        taskMap.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        epicMap.remove(id);
        inMemoryHistoryManager.remove(id);
    }
}
