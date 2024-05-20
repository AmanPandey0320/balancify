package com.kabutar.balancify.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;

public class Context {
    private HttpServer httpServer;

    public Context(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public void setServerContext(){
        this.httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String path = exchange.getRequestURI().getPath();
                exchange.sendResponseHeaders(200, path.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(path.getBytes());
                output.flush();
                exchange.close();
            }
        });
    }
}
