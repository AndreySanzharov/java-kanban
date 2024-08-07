package javakanban.elements;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        updateStatus();
    }

    public void updateSubtask(int index, Subtask subtask) {
        subtaskList.set(index, subtask);
        updateStatus();
    }

    public void deleteSubtask(int index) {
        subtaskList.remove(index);
        updateStatus();
    }

    public void updateStatus() {
        if (subtaskList.isEmpty()) {
            setStatus(Status.NEW);
        } else {
            int statusCounter = 0;
            for (Subtask subtask : subtaskList) {
                if (subtask.getStatus() == Status.NEW) {
                    statusCounter++;
                }
                if (statusCounter == subtaskList.size()) {
                    setStatus(Status.NEW);
                } else {
                    setStatus(Status.IN_PROGRESS);
                }
            }
        }
        int statusCounter = 0;
        for (Subtask subtask : subtaskList) {
            if (subtask.getStatus() == Status.DONE) {
                statusCounter++;
            }
            if (statusCounter == subtaskList.size()) {
                setStatus(Status.DONE);
            }
        }
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }
}