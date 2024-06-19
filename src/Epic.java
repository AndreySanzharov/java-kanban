import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Subtask> subtaskList;

    public Epic(String name, String description, ArrayList<Subtask> subtaskList) {
        super(name, description, Status.IN_PROGRESS);
        this.subtaskList = subtaskList;


        if (subtaskList.isEmpty()) {
            status = Status.NEW;
        } else {
            for (Subtask subtask : subtaskList) {
                int statusCounter = 0;

                if (subtask.status == Status.NEW) {
                    statusCounter++;
                }
                if (statusCounter == (subtaskList.size() - 1)) {
                    status = Status.NEW;
                    break;
                }
            }
        }

        for (Subtask subtask : subtaskList) {
            int statusCounter = 0;
            if (subtask.status == Status.DONE) {
                statusCounter++;
            }
            if (statusCounter == (subtaskList.size() - 1)) {
                status = Status.DONE;
                break;
            }
        }

    }

}






