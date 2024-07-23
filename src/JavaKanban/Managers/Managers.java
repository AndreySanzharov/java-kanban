package JavaKanban.Managers;

import JavaKanban.Interfaces.HistoryManager;
import JavaKanban.Interfaces.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
