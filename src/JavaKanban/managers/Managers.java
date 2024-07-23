package JavaKanban.managers;

import JavaKanban.interfaces.HistoryManager;
import JavaKanban.interfaces.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
