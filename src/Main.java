import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();

        TaskManager taskManager = new TaskManager();
        System.out.println("=======Созданные объекты=======");

        Task task = new Task("Задача", "Задача не делима", Status.NEW);
        taskManager.add(task);
        System.out.println(task);

        Subtask subtask1 = new Subtask("Подзадача I", "####", Status.DONE);
        taskManager.addSubtask(subtask1);
        System.out.println(subtask1);

        Subtask subtask2 = new Subtask("Подзадача II", "****", Status.DONE);
        taskManager.addSubtask(subtask2);
        System.out.println(subtask2);

        subtaskList.add(subtask1);
        subtaskList.add(subtask2);

        Epic epic = new Epic("Эпик", "Эпик делится на подзадачи", subtaskList);
        taskManager.add(epic);
        System.out.println(epic);



        System.out.println("=======Внутри subtaskList=======");
        System.out.println(epic.subtaskList);

        System.out.println("=======Внутри taskMap=======");
        System.out.println(taskManager.taskMap);


        System.out.println("=======Получить все=======");
        System.out.println(taskManager.getAll());

        System.out.println("=======Получить по id=======");
        System.out.println(taskManager.getFromId(1));



        System.out.println("=======Обновить задачу=======");
        Task task2 = new Task("Обновленная задача", "Что-то новое", Status.IN_PROGRESS);
        taskManager.add(task2);
        taskManager.updateTask(1 , task2);
        System.out.println(taskManager.taskMap.get(1));

        System.out.println("=======Обновить эпик=======");
        Epic epic2 = new Epic("Новый эпик", "Я устал", subtaskList);
        taskManager.add(epic2);
        taskManager.updateTask(4,  epic2);
        System.out.println(taskManager.getFromId(4));

        System.out.println("=======Удалить по id=======");
        taskManager.deleteFromId(1);
        System.out.println(taskManager.taskMap);

        System.out.println("=======Удалить все=======");
        taskManager.deleteAll();
        System.out.println(taskManager.taskMap);






















    }
}
