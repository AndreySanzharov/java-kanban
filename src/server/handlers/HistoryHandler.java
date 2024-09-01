package server.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HistoryHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();

        switch (requestMethod) {
            case "GET":
                if (requestURI.equals("/history")) {
                    getHistory(exchange);
                }
        }
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(historyManager.getHistory());
            sendText(exchange, response);
        } catch (IOException exception) {
            String response = "Ошибка получения истории запросов " + exception.getMessage();
            exchange.sendResponseHeaders(400, response.length());
            exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        } finally {
            exchange.close();
        }

    }
}
