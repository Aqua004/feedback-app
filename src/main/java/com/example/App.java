package com.example;

import com.sun.net.httpserver.HttpServer;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", ex -> {
            byte[] page;
            try (InputStream in = App.class.getResourceAsStream("/index.html")) {
                page = in.readAllBytes();
            }
            ex.getResponseHeaders().add("Content-Type", "text/html");
            ex.sendResponseHeaders(200, page.length);
            ex.getResponseBody().write(page);
            ex.close();
        });
        server.createContext("/feedback", ex -> {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Feedback received: " + body);
            byte[] r = "<h3>Thank you for your feedback!</h3><a href='/'>Back</a>".getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().add("Content-Type", "text/html");
            ex.sendResponseHeaders(200, r.length);
            ex.getResponseBody().write(r);
            ex.close();
        });
        server.start();
        System.out.println("Server running on http://localhost:8080");
    }
}
