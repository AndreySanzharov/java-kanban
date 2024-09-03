package javakanban.elements;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private Duration epicDuration;
    private LocalDateTime epicStartTime;
    private LocalDateTime epicEndTime;

    private List<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }


    private void calculateTime() {
        this.epicStartTime = subtaskList.stream().map(Subtask::getEpicStartTime).min(LocalDateTime::compareTo).orElse(null);
        this.epicEndTime = subtaskList.stream().map(Subtask::getEpicEndTime).max(LocalDateTime::compareTo).orElse(null);
        this.epicDuration = subtaskList.stream().map(Subtask::getEpicDuration).reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public Duration getEpicDuration() {
        return epicDuration;
    }

    @Override
    public LocalDateTime getEpicStartTime() {
        return epicStartTime;
    }

    @Override
    public LocalDateTime getEpicEndTime() {
        return epicEndTime;
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        calculateTime();
        updateStatus();
    }

    public void updateSubtask(int index, Subtask subtask) {
        subtaskList.set(index, subtask);
        calculateTime();
        updateStatus();
    }

    public void deleteSubtask(int index) {
        subtaskList.remove(index);
        calculateTime();
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