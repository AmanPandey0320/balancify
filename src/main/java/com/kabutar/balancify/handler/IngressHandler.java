package com.kabutar.balancify.handler;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.provider.Scheduler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class IngressHandler implements HttpHandler {
    private Scheduler scheduler;

    public IngressHandler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        try {
            Server server = this.scheduler.getServeFromPool(path);

            System.out.println(server);
            String res = "aman";
            exchange.sendResponseHeaders(200, res.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            
            output.write(res.getBytes());
            output.flush();
            exchange.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
