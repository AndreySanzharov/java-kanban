package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import javakanban.elements.Status;
import javakanban.elements.Task;
import javakanban.interfaces.TaskManager;
import javakanban.managers.InMemoryTaskManager;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler {
    TaskManager taskManager = new InMemoryTaskManager();

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    //////////Просто для проверки///////////
    Task task = new Task("Task", "Description", Status.NEW, Duration.ofHours(1), LocalDateTime.now());
    Task task2 = new Task("Task2", "Description2", Status.NEW, Duration.ofHours(2), LocalDateTime.now());
    ////////////////////


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        /////////////////////////
        taskManager.addTask(task);
        taskManager.addTask(task2);
        /////////////////////////

        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();

        switch (requestMethod) {
            case "GET":
                if (requestURI.equals("/tasks")) {
                    handleGetAllTasks(exchange);
                } else if (Pattern.matches(("^/tasks/\\d+$"), exchange.getRequestURI().getPath())) {
                    handleGetTaskByID(exchange);
                }
                break;
            case "POST":
                if (requestURI.equals("/tasks")) {
                    handleCreateTask(exchange);
                } else if (Pattern.matches(("^/tasks/\\d+$"), exchange.getRequestURI().getPath())) {
                    handleUpdateTask(exchange);
                }
                break;

            default:
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                break;


        }
    }


    private void handleGetTaskByID(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        String pathId = path.replaceFirst("/tasks/", "");
        int taskID = Integer.parseInt(pathId);
        String response = gson.toJson(taskManager.getTaskById(taskID));
        sendText(exchange, response);
        System.out.println(taskManager.getTaskMap());
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getAll());
        sendText(exchange, response);
        System.out.println(taskManager.getTaskMap());
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Task newTask = gson.fromJson(body, Task.class);
            taskManager.addTask(newTask);

            sendText(exchange, "Задача успешно добавлена c id" + newTask.id);
        } catch (Exception exception) {
            String response = "Ошибка создания задачи: " + exception.getMessage();
            exchange.sendResponseHeaders(400, response.length());
            exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        } finally {
            exchange.close();
        }
    }

    private void handleUpdateTask(HttpExchange exchange){
        try{
            String path = exchange.getRequestURI().getPath();
            String pathId = path.replaceFirst("/tasks/", "");
            int taskID = Integer.parseInt(pathId);

            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Task newTask = gson.fromJson(body, Task.class);
            taskManager.updateTask(taskID, newTask);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
