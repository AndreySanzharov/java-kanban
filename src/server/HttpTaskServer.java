package server;

import com.sun.net.httpserver.HttpServer;


import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    public static HttpServer server;
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler());
        server.start();
    }

    public void start() {
        server.start();
    }

}
