package server.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PrioritizedHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();

        switch (requestMethod) {
            case "GET":
                if (requestURI.equals("/prioritized")) {
                    getPrioritized(exchange);
                }
                break;
        }
    }

    private void getPrioritized(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(exchange, response);
        } catch (RuntimeException exception) {
            String response = "Ошибка получения отфильтрованного списка задач " + exception.getMessage();
            exchange.sendResponseHeaders(400, response.length());
            exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        } finally {
            exchange.close();
        }
    }
}
