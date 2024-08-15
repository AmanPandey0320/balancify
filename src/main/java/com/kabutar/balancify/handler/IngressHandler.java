package com.kabutar.balancify.handler;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.provider.Scheduler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class IngressHandler implements HttpHandler {
    private Scheduler scheduler;

    public IngressHandler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
    
    private String readRequestBody(HttpExchange exchange) throws IOException {
    	StringBuilder builder = new StringBuilder();
    	InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(),"utf-8");
    	
    	int b;
    	
    	while((b = reader.read()) != -1) {
    		builder.append((char) b);
    	}
    	
    	return builder.toString();
    }
    

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Map<String,List<String>> headers = exchange.getRequestHeaders();
        String reqBody = this.readRequestBody(exchange);

        try {
            Server server = this.scheduler.getServeFromPool(path);
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
