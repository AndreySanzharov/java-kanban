public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("=======Созданные объекты=======");

        Task task = new Task("Задача", "Задача не делима", Status.DONE);
        taskManager.addTask(task);
        System.out.println(task);

        Epic epic = new Epic("Эпик", "Эпик делится на подзадачи");
        taskManager.addEpic(epic);
        System.out.println(epic);

        Subtask subtask1 = new Subtask("Подзадача I", "####", Status.NEW);
        taskManager.addSubtask(0, subtask1);
        System.out.println(subtask1);

        Subtask subtask2 = new Subtask("Подзадача II", "****", Status.NEW);
        taskManager.addSubtask(0, subtask2);
        System.out.println(subtask2);


        System.out.println("=======Получить все=======");
        System.out.println(taskManager.getAll());

        System.out.println("=======Получить по id=======");
        System.out.println(taskManager.getEpicById(0));
        System.out.println(taskManager.getTaskById(0));

        System.out.println("=======Обновить подзадачу=======");
        Subtask subtask3 = new Subtask("Новая подзадача", "Что-то новое", Status.IN_PROGRESS);
        taskManager.updateSubtask(0, 0, subtask3);
        System.out.println(taskManager.epicMap);

        System.out.println("=======Удалить подзадачу=======");
        taskManager.deleteSubtask(0, 0);
    }
}
