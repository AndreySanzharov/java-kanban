package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import javakanban.elements.Status;
import javakanban.elements.Task;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandler extends BaseHttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();

        Task task = new Task("Task", "Description", Status.NEW, Duration.ofHours(1), LocalDateTime.now());
        String response = gson.toJson(task);
        sendText(exchange, response);
    }
}
