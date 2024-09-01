package server;

import com.sun.net.httpserver.HttpServer;
import javakanban.interfaces.HistoryManager;
import javakanban.managers.Managers;
import server.handlers.*;


import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    public static HttpServer server;
    private static final int PORT = 8080;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public HttpTaskServer() throws IOException {

        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler());
        server.createContext("/subtasks", new SubtaskHandler());
        server.createContext("/epics", new EpicHandler());
        server.createContext("/history", new HistoryHandler(historyManager));
        server.createContext("/prioritized", new PrioritizedHandler());
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

}
