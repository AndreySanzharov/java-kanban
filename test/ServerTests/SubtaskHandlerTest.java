package ServerTests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javakanban.elements.Epic;
import javakanban.elements.Status;
import javakanban.elements.Subtask;
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

public class SubtaskHandlerTest {
    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer taskServer = new HttpTaskServer();
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpClient client = HttpClient.newHttpClient();


    public SubtaskHandlerTest() throws IOException {
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
    public void getEpicSubtaskByIdTest() throws IOException, InterruptedException {
        // Создание эпика и подзадачи
        Epic epic = new Epic("Epic Name", "Epic Description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask Name", "Subtask Description", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now());
        manager.addSubtask(epic.getId(), subtask);

        // Проверяем, что подзадача добавлена
        Assertions.assertEquals(1, epic.getSubtaskList().size());

        // Получаем ID эпика и ID подзадачи
        int epId = epic.getId();
        int subId = subtask.getId();

        // Создаем URL для получения подзадачи по ID
        URI url = URI.create("http://localhost:8080/subtasks/" + epId + subId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем, что код ответа 200 (ОК)
        Assertions.assertEquals(200, response.statusCode());

        // Десериализуем подзадачу из ответа и проверяем её
        Subtask retrievedSubtask = gson.fromJson(response.body(), Subtask.class);
        Assertions.assertNotNull(retrievedSubtask);
        Assertions.assertEquals(subtask.getName(), retrievedSubtask.getName());
        Assertions.assertEquals(subtask.getDescription(), retrievedSubtask.getDescription());
    }

    @Test
    public void getAllSubtasksOfEpicTest() throws IOException, InterruptedException {
        // Создание эпика и подзадач
        Epic epic = new Epic("Epic Name", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now().plusMinutes(30));

        manager.addSubtask(epic.getId(), subtask1);
        manager.addSubtask(epic.getId(), subtask2);

        // Проверяем, что подзадачи добавлены
        Assertions.assertEquals(2, epic.getSubtaskList().size());

        // Создаем URL для получения всех подзадач эпика
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        List<String> subtasks = new ArrayList<>();
        subtasks.add(response.body());
        Assertions.assertFalse(subtasks.isEmpty(), "Список не должен быть пустым");
    }
}
