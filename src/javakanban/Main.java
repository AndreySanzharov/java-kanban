package javakanban;

import javakanban.elements.Epic;
import javakanban.elements.Status;
import javakanban.elements.Subtask;
import javakanban.elements.Task;
import javakanban.interfaces.TaskManager;
import javakanban.managers.InMemoryHistoryManager;
import javakanban.managers.Managers;

public class Main {


    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("Задача_1", "Описание_1", Status.NEW);
        Task task2 = new Task("Задача_2", "Описание_2", Status.IN_PROGRESS);

        Subtask sub1 = new Subtask("Подзадача_1", "Подзадача_1", Status.DONE);
        Subtask sub2 = new Subtask("Подзадача_2", "Подзадача_2", Status.DONE);
        Subtask sub3 = new Subtask("Подзадача_3", "Подзадача_3", Status.DONE);

        Epic epicWithSub = new Epic("Эпик", "Эпик делится на подзадачи");
        Epic epicWithoutSub = new Epic("Эпик без подзадач", "Нет подзадач");

        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addEpic(epicWithSub);
        inMemoryTaskManager.addEpic(epicWithoutSub);
        inMemoryTaskManager.addSubtask(2, sub1);
        inMemoryTaskManager.addSubtask(2, sub2);
        inMemoryTaskManager.addSubtask(2, sub3);

        inMemoryTaskManager.getTaskById(0);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);

        System.out.println(InMemoryHistoryManager.history);

        inMemoryTaskManager.deleteTaskById(0);
        inMemoryTaskManager.deleteTaskById(1);
        System.out.println(InMemoryHistoryManager.history);

        inMemoryTaskManager.getEpicById(2);
        System.out.println(InMemoryHistoryManager.history);

        inMemoryTaskManager.deleteEpicById(2);

    }
}
