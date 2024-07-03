import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static List<Task> history = new ArrayList<>();
    private final int MAX_SIZE = 9;

    @Override
    public void add(Task task) {
        if (history.size() >= MAX_SIZE){
            history.remove(0);
        }
        history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

}
