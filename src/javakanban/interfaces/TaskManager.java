package javakanban.interfaces;

import javakanban.elements.Epic;
import javakanban.elements.Subtask;
import javakanban.elements.Task;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface TaskManager {

    TreeSet<Task> getPrioritizedTasks();

    Map<Integer, Task> getTaskMap();

    List<Task> getAll();

    void deleteAll();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int epId, int subId);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(int id, Subtask subtask);

    void updateTask(int id, Task task);

    void updateSubtask(int epicId, int subId, Subtask subtask);

    void deleteSubtask(int epicId, int subId);

    void deleteTaskById(int id);

    void deleteEpicById(int id);
}
