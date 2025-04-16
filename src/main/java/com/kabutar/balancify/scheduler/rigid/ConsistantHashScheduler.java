package com.kabutar.balancify.scheduler.rigid;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.event.HealthCheckEvent;
import com.kabutar.balancify.scheduler.BaseScheduler;
import com.kabutar.balancify.workers.HealthCheck;
import com.sun.net.httpserver.HttpExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ConsistantHashScheduler extends BaseScheduler implements HealthCheckEvent{
	//TODO
	private ArrayList<Server> nodes;
	private ArrayList<Integer> keys;
	private HealthCheck healthCheck;
	private ArrayList<Server> servers;
	private int maxPoolSize;

	private static final Logger logger = LogManager.getLogger(ConsistantHashScheduler.class);
	
	
	public ConsistantHashScheduler(ArrayList<Server> servers, HealthCheck healthCheck, int maxPoolSize) {
		super(servers);
		System.out.println("Initializion a consistent hash scheduler with pool size: "+maxPoolSize);
		this.maxPoolSize = maxPoolSize;
		this.healthCheck = healthCheck;
		this.servers = servers;
	}
	
	/**
	 * 
	 * @param server
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	private int getHash(String ip) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(ip.getBytes(StandardCharsets.UTF_8));
        
        int hash = 0;
        for (int i = 0; i < 4; i++) {
            hash <<= 8;
            hash |= (hashBytes[i] & 0xFF);
        }
        return (hash & Integer.MAX_VALUE) % this.maxPoolSize;
	}
	
	/**
	 * 
	 * @param server
	 * @throws Exception
	 */
	protected void removeServer(Server server) throws Exception {
		
		if(this.keys.size() == 0) {
			throw new Exception("No nodes in the server pool");
		}
		
		int key = this.getHash(server.getUrl());
		
		int index = Collections.binarySearch(this.keys, key);
		
		if(index < 0) {
			throw new Exception("Unknown server, not present in nodes");
		}
		
		this.keys.remove(index);
		this.nodes.remove(index);
		
		return;
		
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	protected void addServer(Server server) throws Exception {
		if(keys.size() == this.maxPoolSize) {
			throw new Exception("Max server pool size overflow");
		}

		logger.info("adding new server: {}",server.toString());
		
		int key = this.getHash(server.getUrl());
		int index = Collections.binarySearch(this.keys, key);
		
		
		
		if(index > 0 && this.keys.get(index) == key) {
			throw new Exception("Collosion occured for server "+ server.toString());
		}
		
		int insertionPoint = -(index+1);
		
		this.keys.add(insertionPoint,key);
		this.nodes.add(insertionPoint,server);
		
		return;
	}
	
	
	@Override
	public Server schedule(HttpExchange exchange) throws NoSuchAlgorithmException {
		int key = this.getHash(exchange.getRemoteAddress().getHostName());
		int index = Collections.binarySearch(keys, key);
		
		int nodePoint;
		
		if(index >= 0) {
			nodePoint = index;
		}else {
			nodePoint = ((-(index+1))%this.keys.size());
		}
		return this.servers.get(nodePoint);
		
	}

	@Override
	public void initializeParameters() {
		this.keys = new ArrayList<>();
		this.nodes = new ArrayList<>();
		this.healthCheck.registerEventListner(this);
	}

	@Override
	public void onServerUpEventHandler(Server server) {
		// TODO Auto-generated method stub
		try {
			this.addServer(server);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onServerDownEventHandler(Server server) {
		// TODO Auto-generated method stub
		try {
			this.removeServer(server);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Server> getServerList() {
		// TODO Auto-generated method stub
		return this.servers;
	}
	
}
