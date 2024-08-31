package ServerTests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javakanban.elements.Status;
import javakanban.interfaces.TaskManager;
import javakanban.managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import server.HttpTaskServer;

import javakanban.elements.Task;
import server.adapters.DurationAdapter;
import server.adapters.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TaskHandlerTests {


    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer taskServer = new HttpTaskServer();
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpClient client = HttpClient.newHttpClient();

    public TaskHandlerTests() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAll();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }


    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Status.NEW, Duration.ofMinutes(5),
                LocalDateTime.now());
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
        // проверяем код ответа
        Assertions.assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();
        System.out.println(tasksFromManager);
        Assertions.assertEquals(1, tasksFromManager.size(), "Должна быть одна задача");
    }

    @Test
    public void testGetTaskByID() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", Status.NEW, Duration.ofMinutes(5),
                LocalDateTime.now());
        manager.addTask(task);

        URI url = URI.create("http://localhost:8080/tasks/0");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task recievedTask = gson.fromJson(response.body(), Task.class);

        Assertions.assertEquals(task.id, recievedTask.id, "id должно совпадать");
        Assertions.assertEquals(task.getName(), recievedTask.getName(), "Имя должно совпадать");

        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void updateTasksTest() throws IOException, InterruptedException {
        Task originalTask = new Task("Original Task", "Original Description",
                Status.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        manager.addTask(originalTask);

        Task updatedTask = new Task("Updated Task", "Updated Description",
                Status.IN_PROGRESS, Duration.ofMinutes(60), originalTask.getEpicStartTime());
        updatedTask.setId(0);

        String updatedTaskJson = gson.toJson(updatedTask);

        URI url = URI.create("http://localhost:8080/tasks/" + originalTask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");

        Task taskFromManager = manager.getTaskById(originalTask.getId());

        Assertions.assertEquals("Updated Task", taskFromManager.getName(), "Имя задачи должно быть обновлено");
        Assertions.assertEquals("Updated Description", taskFromManager.getDescription(), "Описание задачи должно быть обновлено");
        Assertions.assertEquals(Status.IN_PROGRESS, taskFromManager.getStatus(), "Статус задачи должен быть обновлен");
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("Task to be deleted", "Description of the task",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);

        List<Task> tasksBeforeDeletion = manager.getTasks();
        Assertions.assertEquals(1, tasksBeforeDeletion.size(), "Перед удалением должна быть одна задача");

        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа при удалении");
        List<Task> tasksAfterDeletion = manager.getTasks();
        Assertions.assertEquals(0, tasksAfterDeletion.size(), "После удаления должна быть 0 задач");
    }

}