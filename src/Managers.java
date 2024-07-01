public class Managers {
    private TaskManager TaskManager;
    private HistoryManager HistoryManager;

    public TaskManager getDefault() {
        return TaskManager;
    }

    public HistoryManager getDefaultHistory() {
        return HistoryManager;
    }

}
