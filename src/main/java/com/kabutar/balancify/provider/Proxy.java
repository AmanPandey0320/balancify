package com.kabutar.balancify.provider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.constants.HttpMethod;
import com.kabutar.balancify.handler.EgressHandler;
import com.kabutar.balancify.util.UrlUtil;
import com.sun.net.httpserver.HttpExchange;

public class Proxy {
	
    private String readRequestBody(HttpExchange exchange) throws IOException {
    	StringBuilder builder = new StringBuilder();
    	InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(),"utf-8");
    	
    	int b;
    	
    	while((b = reader.read()) != -1) {
    		builder.append((char) b);
    	}
    	
    	return builder.toString();
    }
    
    private String makeUrl(HttpExchange exchange, Server server) {
    	StringBuilder builder = new StringBuilder();
    	String path =  exchange.getRequestURI().getPath();
    	String host = UrlUtil.getHostName(server);
    	
    	builder.append(host);
    	builder.append(path);
    	
    	return builder.toString();
    	
    }
	private String makeHttpRequest(HttpExchange exchange, Server server) {
		try {
			String method = exchange.getRequestMethod();
			String url = this.makeUrl(exchange, server);
			
			return "";
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
		return null;
	}
	
	private void applyFilters(HttpExchange exchange) {
		return;
	}
	
	public void execute(HttpExchange exchange,Server server) {
		this.applyFilters(exchange);
		this.makeHttpRequest(exchange, server);
	}
}
