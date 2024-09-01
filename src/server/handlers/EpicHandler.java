package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import javakanban.elements.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();

        switch (requestMethod) {
            case "GET":
                if (requestURI.equals("/epics")) {
                    getAllEpics(exchange);
                } else if (Pattern.matches(("^/epics/\\d+$"), exchange.getRequestURI().getPath())) {
                    getEpicById(exchange);
                } else if (Pattern.matches(("^/epics/\\d+/subtask$"), exchange.getRequestURI().getPath())) {
                    getEpicsSubtasks(exchange);
                }
                break;

            case "POST":
                if (requestURI.equals("/epics")) {
                    createEpic(exchange);
                }
                break;

            case "DELETE":
                if (Pattern.matches("^/epics/\\d+$", exchange.getRequestURI().getPath())) {
                    deleteEpicById(exchange);
                }
                break;

            default:
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                break;
        }
    }

    private void getAllEpics(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getEpics());
        sendText(exchange, response);
        System.out.println(taskManager.getTasks());
    }

    private void getEpicById(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String pathId = path.replaceFirst("/epics/", "");
            int epicId = Integer.parseInt(pathId);
            lastEpicId = epicId;
            String response = gson.toJson(taskManager.getEpicById(epicId));
            sendText(exchange, response);
        } catch (IOException exception) {
            sendNotFound(exchange);
        } finally {
            exchange.close();
        }
    }

    private void getEpicsSubtasks(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String pathWithoutHost = path.substring(path.indexOf("/epics"));

            String[] parts = pathWithoutHost.split("/");

            int id = Integer.parseInt(parts[2]);
            Epic epic = taskManager.getEpicById(id);
            String response = gson.toJson(epic.getSubtaskList());
            sendText(exchange, response);
        } catch (IOException exception) {
            sendNotFound(exchange);
        }
    }

    private void createEpic(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Epic newEpic = gson.fromJson(body, Epic.class);
            taskManager.addEpic(newEpic);

            sendText(exchange, "Эпик успешно добавлен c id" + newEpic.id);
        } catch (IOException exception) {
            String response = "Ошибка создания эпика: " + exception.getMessage();
            exchange.sendResponseHeaders(400, response.length());
            exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        } finally {
            exchange.close();
        }
    }

    private void deleteEpicById(HttpExchange exchange) throws IOException {
        try {
            // Извлечение ID из URI запроса
            String path = exchange.getRequestURI().getPath();
            String pathId = path.replaceFirst("/epics/", "");
            int epicId = Integer.parseInt(pathId);

            // Удаление эпика из TaskManager
            if (taskManager.getEpicById(epicId) != null) {
                taskManager.deleteEpicById(epicId);

                // Успешный ответ
                String response = "Эпик успешно удален";
                sendText(exchange, response);
            } else {
                // Эпик не найден
                sendNotFound(exchange);
            }
        } catch (IOException e) {
            sendNotFound(exchange);
        }
    }

}
