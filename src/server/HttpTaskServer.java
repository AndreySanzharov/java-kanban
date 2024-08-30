package server;

import com.sun.net.httpserver.HttpServer;
import server.handlers.EpicHandler;
import server.handlers.HistoryHandler;
import server.handlers.SubtaskHandler;
import server.handlers.TaskHandler;
import server.handlers.PrioritizedHandler;



import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    public static HttpServer server;
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler());
        server.createContext("/subtasks", new SubtaskHandler());
        server.createContext("/epics", new EpicHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedHandler());
        server.start();
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

}
