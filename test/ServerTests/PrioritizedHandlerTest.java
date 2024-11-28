package ServerTests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javakanban.elements.Status;
import javakanban.elements.Task;
import javakanban.interfaces.TaskManager;
import javakanban.managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import server.adapters.DurationAdapter;
import server.adapters.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

class PrioritizedHandlerTest {
    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer taskServer = new HttpTaskServer();
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpClient client = HttpClient.newHttpClient();

    public PrioritizedHandlerTest() throws IOException {
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
    void shouldReturnPrioritizedTasks() throws IOException, InterruptedException {
        // Создаем задачи и добавляем их в TaskManager
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(2),
                LocalDateTime.of(2024, 9, 1, 10, 0));

        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(2),
                LocalDateTime.of(2024, 9, 1, 12, 0));

        manager.addTask(task1);
        manager.addTask(task2);

        // Отправляем GET запрос к серверу
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        String expectedResponse = gson.toJson(manager.getPrioritizedTasks());

        Assertions.assertEquals(expectedResponse, response.body());
    }
}
