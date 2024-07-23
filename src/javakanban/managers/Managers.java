package javakanban.managers;

import javakanban.interfaces.HistoryManager;
import javakanban.interfaces.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
