package javakanban.managers;

import exceptions.ManagerSaveException;
import javakanban.elements.Task;
import javakanban.elements.Epic;
import javakanban.elements.Subtask;
import javakanban.elements.Status;
import javakanban.elements.Types;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FileBackedTaskManager extends InMemoryTaskManager {
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    //дополнительное задание
    public static void main(String[] args) throws IOException {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("task1", "task1 desc", Status.NEW,
                Duration.ofMinutes(55), LocalDateTime.of(2024, 11, 10, 10, 0));
        Epic epic1 = new Epic("epic1", "epic1 desc");
        Subtask sub1 = new Subtask("sub1", "sub1 desc", Status.DONE,
                Duration.ofMinutes(55), LocalDateTime.of(2024, 11, 12, 11, 0));
        ;

        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addSubtask(1, sub1);

        System.out.println(inMemoryTaskManager.taskMap);
        System.out.println(inMemoryTaskManager.epicMap);
        System.out.println(epic1.getSubtaskList() + "\n");

        File file = new File("src/javakanban/save/load.csv");

        loadFromFile(file);

        System.out.println(taskMap);
        System.out.println(epicMap);
    }


    public static void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("src/javakanban/save/save.csv"))) {
            fileWriter.write("id,type,name,status,description,startTime,endTime,duration\n");

            // Сохранение задач
            for (Integer id : taskMap.keySet()) {
                Task task = taskMap.get(id);
                fileWriter.write(id + "," +
                        defineType(task) + "," +
                        task.getName() + "," +
                        task.getStatus() + "," +
                        task.getDescription() + "," +
                        (task.getStartTime() != null ? task.getStartTime().format(FORMATTER) : "") + "," +
                        (task.getEndTime() != null ? task.getEndTime().format(FORMATTER) : "") + "," +
                        (task.getDuration() != null ? task.getDuration().toMinutes() : 0) + "\n");
            }

            // Сохранение эпиков и подзадач
            for (Integer id : epicMap.keySet()) {
                Epic epic = epicMap.get(id);
                fileWriter.write(id + "," +
                        defineType(epic) + "," +
                        epic.getName() + "," +
                        epic.getStatus() + "," +
                        epic.getDescription() + "," +
                        (epic.getStartTime() != null ? epic.getStartTime().format(FORMATTER) : "") + "," +
                        (epic.getEndTime() != null ? epic.getEndTime().format(FORMATTER) : "") + "," +
                        (epic.getDuration() != null ? epic.getDuration().toMinutes() : 0) + "\n");

                for (Subtask sub : epic.getSubtaskList()) {
                    fileWriter.write(sub.getId() + "," +
                            defineType(sub) + "," +
                            sub.getName() + "," +
                            sub.getStatus() + "," +
                            sub.getDescription() + "," +
                            (sub.getStartTime() != null ? sub.getStartTime().format(FORMATTER) : "") + "," +
                            (sub.getEndTime() != null ? sub.getEndTime().format(FORMATTER) : "") + "," +
                            (sub.getDuration() != null ? sub.getDuration().toMinutes() : 0) + "\n");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage(), e.getCause());
        }
    }


    public static void loadFromFile(File file) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            int lastEpicId = -1; // Переменная для отслеживания последнего эпика

            bufferedReader.readLine(); // Пропускаем заголовок
            while ((line = bufferedReader.readLine()) != null) {
                String[] infoString = line.split(",");

                if (infoString.length < 7) {
                    continue; // Пропустить строки с недостаточным количеством данных
                }

                Integer id = Integer.parseInt(infoString[0]);
                String type = infoString[1];
                String name = infoString[2];
                String status = infoString[3];
                String description = infoString[4];

                LocalDateTime startTime = null;
                LocalDateTime endTime = null;
                Duration duration = Duration.ZERO;

                if (!infoString[5].isEmpty()) {
                    startTime = LocalDateTime.parse(infoString[5], FORMATTER);
                }
                if (!infoString[6].isEmpty()) {
                    endTime = LocalDateTime.parse(infoString[6], FORMATTER);
                }
                if (!infoString[7].isEmpty()) {
                    duration = Duration.ofMinutes(Long.parseLong(infoString[7]));
                }


                if ("TASK".equals(type)) {
                    taskMap.put(id, new Task(name, description, Status.valueOf(status), duration, startTime));
                } else if ("EPIC".equals(type)) {
                    Epic epic = new Epic(name, description);
                    epicMap.put(id, epic);
                    lastEpicId = id;
                } else if ("SUBTASK".equals(type)) {
                    Subtask subtask = new Subtask(name, description, Status.valueOf(status), duration, startTime);
                    Epic epic = epicMap.get(lastEpicId);
                    if (epic != null) {
                        epic.addSubtask(subtask);
                    }
                }
            }
        }
    }

    private static Types defineType(Task task) {
        Types status;
        if (task.getClass() == Task.class) {
            status = Types.TASK;
        } else if (task.getClass() == Epic.class) {
            status = Types.EPIC;
        } else {
            status = Types.SUBTASK;
        }
        return status;
    }
}
