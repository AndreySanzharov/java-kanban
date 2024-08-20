package javakanban;

import javakanban.elements.Epic;
import javakanban.elements.Status;
import javakanban.elements.Subtask;
import javakanban.elements.Task;
import javakanban.interfaces.TaskManager;
import javakanban.managers.InMemoryHistoryManager;
import javakanban.managers.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

import static javakanban.managers.InMemoryTaskManager.epicMap;

public class Main {


    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("Задача_1", "Описание_1", Status.NEW,
                Duration.ofMinutes(55), LocalDateTime.of(2024, 11, 10, 10, 0));
        Task task2 = new Task("Задача_2", "Описание_2", Status.IN_PROGRESS,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 11, 10, 11, 0));

        Subtask sub1 = new Subtask("Подзадача_1", "Подзадача_1", Status.DONE,
                Duration.ofMinutes(55),  LocalDateTime.of(2024, 11, 12, 11, 0));
        Subtask sub2 = new Subtask("Подзадача_2", "Подзадача_2", Status.DONE,
                Duration.ofMinutes(55),  LocalDateTime.of(2024, 11, 12, 12, 0));
        Subtask sub3 = new Subtask("Подзадача_3", "Подзадача_3", Status.DONE,
                Duration.ofMinutes(55),  LocalDateTime.of(2024, 11, 12, 13, 0));

        Epic epicWithSub = new Epic("Эпик", "Эпик делится на подзадачи");
        Epic epicWithoutSub = new Epic("Эпик без подзадач", "Нет подзадач");

        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.getTaskById(0);

        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.getTaskById(1);

        inMemoryTaskManager.addEpic(epicWithSub);
        inMemoryTaskManager.getEpicById(2);

        inMemoryTaskManager.addSubtask(2, sub1);
        inMemoryTaskManager.addSubtask(2, sub2);
        inMemoryTaskManager.addSubtask(2, sub3);

    }
}
