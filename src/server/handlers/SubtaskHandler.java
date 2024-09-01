package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import javakanban.elements.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class SubtaskHandler extends BaseHttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();

        switch (requestMethod) {
            case "GET":
                if (requestURI.equals("/subtasks")) {
                    handleGetAllSubtasks(exchange);
                } else if (Pattern.matches(("^/subtasks/\\d+$"), exchange.getRequestURI().getPath())) {
                    handleGetSubtaskById(exchange);
                }
                break;
            case "POST":
                if (requestURI.equals("/tasks")) {
                    handleCreateSubtask(exchange);
                } else if (Pattern.matches(("^/subtasks/\\d+$"), exchange.getRequestURI().getPath())) {
                    handleUpdateSubtask(exchange);
                }
                break;
            case "DELETE":
                if (Pattern.matches(("^/subtasks/\\d+$"), exchange.getRequestURI().getPath())) {
                    handleDeleteSubtaskById(exchange);
                }
            default:
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                break;
        }
    }

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        /* поскольку мы можем обращаться в подзадачам через эпик, я решил, что будем обращаться к последнему эпику,
         чтобы не передавать в адрес лишнюю информацию*/
        String response = gson.toJson(taskManager.getSubtasks(lastEpicId));
        sendText(exchange, response);
        System.out.println(taskManager.getSubtasks(lastEpicId));
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            // Извлечение идентификаторов эпика и подзадачи из пути
            String pathId = path.replaceFirst("/subtasks/", "");
            String epIdStr = pathId.substring(0, 1); // Первая цифра - это ID эпика
            String subIdStr = pathId.substring(1); // Остальная часть строки - ID подзадачи

            int epId = Integer.parseInt(epIdStr);
            int subId = Integer.parseInt(subIdStr);

            // Получение подзадачи по ID эпика и ID подзадачи
            Subtask subtask = taskManager.getSubtaskById(epId, subId);

            if (subtask != null) {
                String response = gson.toJson(subtask);
                sendText(exchange, response);
            } else {
                sendNotFound(exchange);
            }
        } catch (IOException | NumberFormatException exception) {
            sendNotFound(exchange);
        } finally {
            exchange.close();
        }
    }


    private void handleCreateSubtask(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String pathId = path.replaceFirst("/subtasks/", "");
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            int twoId = Integer.parseInt(pathId);
            int epId = twoId / 10;
            Subtask subtask = gson.fromJson(body, Subtask.class);
            taskManager.addSubtask(epId, subtask);
        } catch (Exception exception) {
            sendNotFound(exchange);
        } finally {
            exchange.close();
        }
    }

    private void handleUpdateSubtask(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String pathId = path.replaceFirst("/subtasks/", "");

            String epIdStr = pathId.substring(0, 1);
            String subIdStr = pathId.substring(1, 2);

            int epId = Integer.parseInt(epIdStr);
            int subId = Integer.parseInt(subIdStr);

            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Subtask subtask = gson.fromJson(body, Subtask.class);
            taskManager.updateSubtask(epId, subId, subtask);
        } catch (Exception exception) {
            sendNotFound(exchange);
        } finally {
            exchange.close();
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String pathId = path.replaceFirst("/subtasks/", "");
            int twoId = Integer.parseInt(pathId);
            int epId = twoId / 10;
            int subId = twoId % 10;
            taskManager.deleteSubtask(epId, subId);
            sendText(exchange, "Подзадача успешно удалена");
        } catch (Exception exception) {
            sendNotFound(exchange);
        } finally {
            exchange.close();
        }
    }


}
