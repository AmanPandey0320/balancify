package com.kabutar.balancify.provider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.handler.EgressHandler;
import com.kabutar.balancify.workers.LoadMonitor;
import com.sun.net.httpserver.HttpExchange;

public class Proxy {
	
	private LoadMonitor loadMonitor;
	

	public void setLoadMonitor(LoadMonitor loadMonitor) {
		this.loadMonitor = loadMonitor;
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
    
    private String makeUrl(HttpExchange exchange, Server server) {
    	StringBuilder builder = new StringBuilder();
    	String path =  exchange.getRequestURI().getPath();
    	String host = server.getUrl();
    	
    	builder.append(host);
    	builder.append(path);
    	
    	return builder.toString();
    	
    }
    
    /**
     * @descriptoin 
     * sends egress req to scheduled server, 
     * manages load via load monitor
     * @param exchange
     * @param server
     * @return
     */
	private String makeHttpRequest(HttpExchange exchange, Server server) {
		try {
			String method = exchange.getRequestMethod();
			String url = this.makeUrl(exchange, server);
			String reqParam = this.readRequestBody(exchange);
			Map<String,List<String>> reqProps = exchange.getRequestHeaders();
			
			//incease load on server
			this.loadMonitor.increase(server.getId());
			
			String egressResponse = EgressHandler.handle(url, method, reqProps, reqParam);
			
			//decrease load on server;
			this.loadMonitor.decrease(server.getId());
			
			return egressResponse;
		}catch(Exception e) {
			//decrease load on server;
			this.loadMonitor.decrease(server.getId());
			
			System.out.print(e.getMessage());
		}
		return null;
	}
	
	private boolean applyFilters(HttpExchange exchange) {
		// TODO: add filters to load balancer, if fail return false
		return true;
	}
	
	public String execute(HttpExchange exchange,Server server) {
		if(this.applyFilters(exchange)) {
			return this.makeHttpRequest(exchange, server);
		}else {
			return null;
		}
	}
}
