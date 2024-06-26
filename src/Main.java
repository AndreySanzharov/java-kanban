public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

        System.out.println("=======Созданные объекты=======");

        Task task = new Task("Задача", "Задача не делима", Status.DONE);
        taskManager.addTask(task);
        System.out.println(task);

        Epic epic = new Epic("Эпик", "Эпик делится на подзадачи");
        taskManager.addEpic(epic);
        System.out.println(epic);

        Subtask subtask1 = new Subtask("I", "####", Status.NEW);
        taskManager.addSubtask(1, subtask1);
        System.out.println(subtask1);

        Subtask subtask2 = new Subtask("II", "****", Status.NEW);
        taskManager.addSubtask(1, subtask2);
        System.out.println(subtask2);

        System.out.println("=======Подзадачи эпика=======");
        System.out.println(epic.subtaskList);

        System.out.println("=======Получить задачу/эпик по id=======");
        System.out.println(taskManager.getTaskById(0));
        System.out.println(taskManager.getEpicById(1));

        System.out.println("=======Обновить задачу=======");
        Task task2 = new Task("Задача2", "JJJ", Status.IN_PROGRESS);
        taskManager.updateTask(0, task2);
        System.out.println(taskManager.taskMap);

        System.out.println("=======Обновить подзадачу=======");
        Subtask subtask3 = new Subtask("AAA", "AAA", Status.DONE);
        taskManager.updateSubtask(1, 0, subtask3);

        System.out.println(epic.subtaskList);
        System.out.println(taskManager.epicMap);

        System.out.println("=======Удалить подзадачу=======");
        taskManager.deleteSubtask(1, 0);
        System.out.println(epic.subtaskList);


    }
}