package javakanban.managers;

import Exceptions.ManagerSaveException;
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


public class FileBackedTaskManager extends InMemoryTaskManager {
    //дополнительное задание
    public static void main(String[] args) throws IOException {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("task1", "task1 desc", Status.NEW);
        Epic epic1 = new Epic("epic1", "epic1 desc");
        Subtask sub1 = new Subtask("sub1", "sub1 desc", Status.DONE);

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
            fileWriter.write("id,type,name,status,description\n");

            if (!taskMap.isEmpty() & (taskMap != null)) {
                for (Integer id : taskMap.keySet()) {
                    fileWriter.write(id + "," +
                            defineType(taskMap.get(id)) + "," +
                            taskMap.get(id).getName() + "," +
                            taskMap.get(id).getStatus() + "," +
                            taskMap.get(id).getDescription() + "\n");
                }
            }

            if ((!epicMap.isEmpty()) & (epicMap != null)) {
                for (Integer id : epicMap.keySet()) {
                    fileWriter.write(id + "," +
                            defineType(epicMap.get(id)) + "," +
                            epicMap.get(id).getName() + "," +
                            epicMap.get(id).getStatus() + "," +
                            epicMap.get(id).getDescription() + "\n");

                    if (!epicMap.get(id).getSubtaskList().isEmpty() & (epicMap.get(id).getSubtaskList() != null)) {
                        for (Subtask sub : epicMap.get(id).getSubtaskList()) {
                            fileWriter.write(sub.getId() + "," +
                                    defineType(sub) + "," +
                                    sub.getName() + "," +
                                    sub.getStatus() + "," +
                                    sub.getDescription() + "\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage(), e.getCause());
        }
    }


    public static void loadFromFile(File file) throws IOException {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            int lastEpicId = 0; // эта переменная нужна, чтобы определить, в какой эпик класть подзадачу из файла
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String[] infoString = bufferedReader.readLine().split(",");

                Integer id = Integer.parseInt(infoString[0]);
                String type = infoString[1];
                String name = infoString[2];
                String status = infoString[3];
                String description = infoString[4];


                if ("TASK".equals(type)) {
                    taskMap.put(id, new Task(name, description, Status.valueOf(status)));
                } else if ("EPIC".equals(type)) {
                    epicMap.put(id, new Epic(name, description));
                    lastEpicId = id;
                } else if ("SUBTASK".equals(type)) {
                    inMemoryTaskManager.addSubtask(lastEpicId, new Subtask(name, description, Status.valueOf(status)));
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
