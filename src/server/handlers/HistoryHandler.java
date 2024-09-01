package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import javakanban.interfaces.HistoryManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private final HistoryManager historyManager;

    public HistoryHandler(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();

        if ("GET".equals(requestMethod) && "/history".equals(requestURI)) {
            getHistoryHandler(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    private void getHistoryHandler(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(historyManager.getHistory());
            sendText(exchange, response);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }
}
