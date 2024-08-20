package javakanban.elements;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;
    private String description;
    private Status status;
    public int id;
    private LocalDateTime startTime;
    private Duration duration;
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");


    public Task(String name, String description, Status status) {
        this.status = status;
        this.description = description;
        this.name = name;
    }

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this(name, description, status);
        this.duration = duration;
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if ((startTime == null) && (duration == null)) {
            return null;
        } else {
            return startTime.plus(duration);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass() + "{" +
                "name= '" + name + '\'' +
                ", desctiption= '" + description + '\'' +
                ", status= '" + status + '\'' +
                ", startTime= '" + getStartTime().format(FORMATTER) + '\'' +
                ", endTime= '" + getEndTime().format(FORMATTER) + '\'' +
                "}";
    }
}
