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
                if (requestURI.equals("/subtsks")) {
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
        /*тут уже вынуждены передать "двойной" id
        первая цифра будет индексом эпика, а вторая - подзадачи */
        try {
            String path = exchange.getRequestURI().getPath();

            String pathId = path.replaceFirst("/subtasks/", "");

            int twoId = Integer.parseInt(pathId);

            int epId = twoId / 10;
            int subId = twoId % 10;

            String response = gson.toJson(taskManager.getSubtaskById(epId, subId));
            sendText(exchange, response);
        } catch (IOException exception) {
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
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            int twoId = Integer.parseInt(pathId);
            int epId = twoId / 10;
            int subId = twoId % 10;
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
