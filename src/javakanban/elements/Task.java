package javakanban.elements;

public class Task {
    private String name;
    private String description;
    private Status status;
    public int id;

    public Task(String name, String description, Status status) {
        this.status = status;
        this.description = description;
        this.name = name;
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
                ", status = '" + status + '\'' +
                "}";
    }

}
