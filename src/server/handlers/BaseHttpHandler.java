package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javakanban.interfaces.HistoryManager;
import javakanban.interfaces.TaskManager;
import javakanban.managers.Managers;
import server.adapters.DurationAdapter;
import server.adapters.LocalDateTimeAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler implements HttpHandler {
    protected TaskManager taskManager = Managers.getDefault();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected static int lastEpicId = 0;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException{
        String resp = "Объект не найден";
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, resp.length());
        exchange.getResponseBody().write(resp.getBytes());
        exchange.close();
    }
    protected void sendHasInteractions(HttpExchange exchange) throws IOException{
        String resp = "Задачи пересекаются";
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(406, resp.length());
        exchange.getResponseBody().write(resp.getBytes());
        exchange.close();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}