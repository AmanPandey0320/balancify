package com.kabutar.balancify.provider;

import com.kabutar.balancify.config.Server;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class RequestHandler implements HttpHandler {
    private Scheduler scheduler;

    public RequestHandler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        try {
            Server server = this.scheduler.getServeFromPool(path);

            System.out.println(server);

            exchange.sendResponseHeaders(200, path.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(path.getBytes());
            output.flush();
            exchange.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
