import java.util.ArrayList;

public class Task {
    String name;
    String description;
    Status status;
    int id;

    public Task(String name, String description, Status status) {
        this.status = status;
        this.description = description;
        this.name = name;
    }


    @Override
    public String toString() {
        return getClass() + "{" +
                "name= '" + name + '\'' +
                ", desctiption= '" + description + '\'' +
                ", status = '" + status + '\'' +
                ", id = '" + id + '\'' +
                "}";
    }

}
