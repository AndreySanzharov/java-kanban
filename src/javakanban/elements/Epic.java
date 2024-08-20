package javakanban.elements;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }


    private void calculateTime() {
        this.startTime = subtaskList.stream().map(Subtask::getStartTime).min(LocalDateTime::compareTo).orElse(null);
        this.endTime = subtaskList.stream().map(Subtask::getEndTime).max(LocalDateTime::compareTo).orElse(null);
        this.duration = subtaskList.stream().map(Subtask::getDuration).reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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