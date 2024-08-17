package com.kabutar.balancify.handler;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.provider.Proxy;
import com.kabutar.balancify.provider.Scheduler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class IngressHandler implements HttpHandler {
    private Scheduler scheduler;
    private Proxy proxy;

    public IngressHandler(Scheduler scheduler, Proxy proxy) {
        this.scheduler = scheduler;
        this.proxy = proxy;
    }
    

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        

        try {
            Server server = this.scheduler.getServeFromPool(path);
            String res = this.proxy.execute(exchange, server);
            exchange.sendResponseHeaders(200, res.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            
            output.write(res.getBytes());
            output.flush();
            exchange.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
