import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    int ageOfTask = 0; // чтобы удалить самый старый элемент, создадим условный возраст элемента

    @Override
    public void add(Task task) {
        if (ageOfTask >= 9) { // дошли до последней задачи - обнулили счетчик
            ageOfTask = 0;
        }
        if (history.size() < 9) {
            history.add(task);
        } else {
            history.set(ageOfTask, task);
            ageOfTask++;
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

}
