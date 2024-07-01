import java.util.ArrayList;

public interface HistoryManager {
    ArrayList<Task> history = new ArrayList<>();

    public void add(Task task);

    public ArrayList<Task> getHistory();
}
