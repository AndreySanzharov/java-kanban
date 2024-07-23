package JavaKanban;

import JavaKanban.Elements.Epic;
import JavaKanban.Managers.InMemoryTaskManager;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Эпик делится на подзадачи");

        inMemoryTaskManager.addEpic(epic);

        System.out.println(inMemoryTaskManager.epicMap);
        System.out.println(inMemoryTaskManager.getEpicById(0));

    }
}
