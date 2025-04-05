package com.kabutar.balancify.scheduler.rigid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.scheduler.BaseScheduler;
import com.kabutar.balancify.workers.HealthCheck;


/**
 * 
 * @author Aman Kr Pandey
 *
 */
public class LinearHashScheduler extends BaseScheduler {
	private ArrayList<Server> servers;
    private HealthCheck healthCheck;
    private Map<Integer,Server> serverMap;
    private Map<String,Server> reqToServerMap;
    

	public LinearHashScheduler(ArrayList<Server> servers, HealthCheck healthCheck) {
		super(servers);
		this.servers = servers;
		this.healthCheck = healthCheck;
	}
	
	private void prepareServerHash() {
		for(int i=0;i<this.servers.size();i++) {
			if(healthCheck.isServerHealthy(this.servers.get(i).getId())){
				this.serverMap.put(i, servers.get(i));
			}
		}
	}

	@Override
	public Server schedule(HttpExchange exchange) throws IOException {
		String host = exchange.getRemoteAddress().getHostName();
		int hash = host.hashCode();
		int key = (hash%(this.serverMap.size()));
		return this.serverMap.get(key);
		
	}

	@Override
	public void initializeParameters() {
		this.serverMap = new HashMap<>();
		this.prepareServerHash();
	}
	
	
	
	
	
}
