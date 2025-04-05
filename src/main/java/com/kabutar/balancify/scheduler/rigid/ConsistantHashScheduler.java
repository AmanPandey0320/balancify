package com.kabutar.balancify.scheduler.rigid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.scheduler.BaseScheduler;
import com.kabutar.balancify.workers.HealthCheck;
import com.sun.net.httpserver.HttpExchange;

public class ConsistantHashScheduler extends BaseScheduler {
	//TODO
	private ArrayList<Integer> servers;
	private HealthCheck healthCheck;
	private TreeSet<Integer> serverSet;
	private Map<Integer,Server> serverMap;
	private int maxPoolSize;
	
	
	public ConsistantHashScheduler(ArrayList<Server> servers, HealthCheck healthCheck, int maxPoolSize) {
		super(servers);
		System.out.println("Initializion a consistent hash scheduler with pool size: "+maxPoolSize);
		this.maxPoolSize = maxPoolSize;
		this.healthCheck = healthCheck;
		this.prepareServers(servers);
	}
	
	/**
	 * 
	 * @param server
	 * @return
	 */
	private int getHash(Server server) {
		int hash = server.getIp().hashCode();
		
		return (hash%this.maxPoolSize);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	private void addServer(Server server) throws Exception {
		if(serverSet.size() == this.maxPoolSize) {
			throw new Exception("Max server pool size overflow");
		}
		
		int key = this.getHash(server);
		int index = this.serverSet.headSet(key,true).size();
		
		if(index > 0 && this.servers.get(index) == key) {
			throw new Exception("Collosion occured for server "+ server.toString());
		}
		
		servers.add(index,key);
		serverMap.put(index, server);
		
		return;
	}
	
	/**
	 * 
	 */
	private void prepareServers(ArrayList<Server> servers) {
		for(Server server: servers) {
			try {
				this.addServer(server);
			} catch (Exception e) {
				// TODO handle exception
				e.printStackTrace();
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
	}
	
}
