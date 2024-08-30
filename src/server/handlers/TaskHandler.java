package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import javakanban.elements.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

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
            case "DELETE":
                if (Pattern.matches(("^/tasks/\\d+$"), exchange.getRequestURI().getPath())) {
                    handleDeleteTaskById(exchange);
                }
            default:
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                break;
        }
    }


    private void handleGetTaskByID(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            String pathId = path.replaceFirst("/tasks/", "");
            int taskID = Integer.parseInt(pathId);
            String response = gson.toJson(taskManager.getTaskById(taskID));
            sendText(exchange, response);
            System.out.println(taskManager.getTasks());
        } catch (IOException exception) {
            sendNotFound(exchange);
        } finally {
            exchange.close();
        }

    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getTasks());
        sendText(exchange, response);
        System.out.println(taskManager.getTasks());
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Task newTask = gson.fromJson(body, Task.class);
            taskManager.addTask(newTask);

            sendText(exchange, "Задача успешно добавлена c id" + newTask.id);
        } catch (IOException exception) {
            String response = "Ошибка создания задачи: " + exception.getMessage();
            exchange.sendResponseHeaders(400, response.length());
            exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        } finally {
            exchange.close();
        }
    }

    private void handleUpdateTask(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String pathId = path.replaceFirst("/tasks/", "");
            int taskID = Integer.parseInt(pathId);

            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Task newTask = gson.fromJson(body, Task.class);
            taskManager.updateTask(taskID, newTask);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            exchange.close();
        }
    }

    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String pathId = path.replaceFirst("/tasks/", "");
            int taskID = Integer.parseInt(pathId);
            taskManager.deleteTaskById(taskID);
            sendText(exchange, "Задача успешно удалена");
        } catch (Exception exception) {
            sendNotFound(exchange);
        } finally {
            exchange.close();
        }
    }
}
