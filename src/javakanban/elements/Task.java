package javakanban.elements;

public class Task {
    String name;
    String description;
    Status status;
    public int id;

    public Task(String name, String description, Status status) {
        this.status = status;
        this.description = description;
        this.name = name;
    }

    public int getId() {
        return id;
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
