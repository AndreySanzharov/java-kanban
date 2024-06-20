import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        updateStatus();
    }

    public void addSubtask(Subtask subtask){
        subtaskList.add(subtask);
        updateStatus();
    }

    public void updateSubtask(int index, Subtask subtask){
        subtaskList.set(index, subtask);
        updateStatus();
    }
    public void deleteSubtask(int index){
        subtaskList.remove(index);
        updateStatus();
    }

    private void updateStatus(){

        if (subtaskList.isEmpty()) {
            status = Status.NEW;
        } else {
            int statusCounter = 0;
            for (Subtask subtask : subtaskList) {
                if (subtask.status == Status.NEW) {
                    statusCounter++;
                }
                if (statusCounter == subtaskList.size()) {
                    status = Status.NEW;
                }else{
                    status = Status.IN_PROGRESS;
                }
            }
        }

        int statusCounter = 0;
        for (Subtask subtask : subtaskList) {
            if (subtask.status == Status.DONE) {
                statusCounter++;
            }
            if (statusCounter == subtaskList.size()) {
                status = Status.DONE;
            }
        }





    }
}