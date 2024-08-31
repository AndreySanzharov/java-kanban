package ServerTests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javakanban.elements.Epic;
import javakanban.elements.Status;
import javakanban.elements.Subtask;
import javakanban.elements.Task;
import javakanban.interfaces.TaskManager;
import javakanban.managers.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.ArrayList;
import java.util.List;

public class EpicHandlerTests {
    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer taskServer = new HttpTaskServer();
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpClient client = HttpClient.newHttpClient();

    public EpicHandlerTests() throws IOException {
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
    public void addEpicTest() throws IOException, InterruptedException {
        // Создаем новый эпик
        Epic epic = new Epic("Epic Name", "Epic Description");
        Subtask subtask = new Subtask("Sub", "Desc", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        epic.addSubtask(subtask);

        System.out.println(epic);

        // Преобразуем эпик в JSON
        String epicJson = gson.toJson(epic);

        // Создаем HTTP-запрос для создания эпика
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        // Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем, что статус код ответа равен 200
        Assertions.assertEquals(200, response.statusCode(), "Некорректный статус код ответа");

        // Проверяем, что эпик был добавлен
        List<Task> epicsFromManager = manager.getEpics();
        Assertions.assertEquals(1, epicsFromManager.size(), "Должен быть один эпик");
        System.out.println(epicsFromManager);
    }

    @Test
    public void getAllEpicsTest() throws IOException, InterruptedException {
        Epic epic = new Epic("EpicToGet", "Epic Description");
        Subtask subtask = new Subtask("Sub", "Desc", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        epic.addSubtask(subtask);
        manager.addEpic(epic);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        ArrayList<Task> expectedEpics = new ArrayList<>(manager.getEpics());
        String expectedJson = gson.toJson(expectedEpics);
        Assertions.assertEquals(expectedJson, response.body());
    }

    @Test
    public void getEpicsSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("EpicToGet", "Epic Description");
        Subtask subtask = new Subtask("Sub", "Desc", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        epic.addSubtask(subtask);
        manager.addEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/0/subtask");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        String expectedJson = gson.toJson(manager.getEpicById(0).getSubtaskList());
        Assertions.assertEquals(expectedJson, response.body());
    }

    @Test
    public void deleteEpicByIdTest() throws IOException, InterruptedException {
        Epic epic = new Epic("EpicToDelete", "Epic Description");
        Subtask subtask = new Subtask("Sub", "Desc", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        epic.addSubtask(subtask);
        manager.addEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        List<Task> epicsFromManager = manager.getEpics();
        System.out.println(epicsFromManager);
        Assertions.assertTrue(epicsFromManager.isEmpty(), "Список эпиков должен быть пуст");
    }

}
