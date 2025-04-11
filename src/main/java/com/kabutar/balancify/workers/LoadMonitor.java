package com.kabutar.balancify.workers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.kabutar.balancify.config.Server;


public class LoadMonitor {
	
	private Map<String,Integer> serverToIndex;
	private ArrayList<Integer> reqCount;
	private ArrayList<Server> servers;
	
	/**
	 * @constructor
	 * @param requestCount
	 */
	public LoadMonitor() {
		super();
		this.reqCount = new ArrayList<>(Collections.nCopies(servers.size(), 0));
		this.serverToIndex = new HashMap<>();
	}
	
	/**
	 * 
	 */
	public void addServers(ArrayList<Server> servers) {
		int index = this.servers.size();
		for(Server server:servers) {
			this.servers.add(server);
			this.serverToIndex.put(server.getId(), index);
			index++;
		}
	}
	
	/**
	 * 
	 * @param id
	 */
	public void increase(String id) {
		int currIndex = this.serverToIndex.get(id);
		int cnt = this.reqCount.get(currIndex) + 1;
		Server server = this.servers.get(currIndex);
		
		this.reqCount.remove(currIndex);
		this.servers.remove(currIndex);
		
		int newIndex = Collections.binarySearch(this.reqCount, cnt);
		
		if(newIndex < 0) {
			newIndex = -(newIndex+1);
		}
		
		this.reqCount.add(newIndex, cnt);
		this.servers.add(newIndex, server);
		this.serverToIndex.put(id, newIndex);
		
		return;
		
	}
	
	/**
	 * 
	 * @param id
	 */
	public void decrease(String id) {
		int currIndex = this.serverToIndex.get(id);
		int cnt = this.reqCount.get(currIndex) - 1;
		Server server = this.servers.get(currIndex);
		
		this.reqCount.remove(currIndex);
		this.servers.remove(currIndex);
		
		int newIndex = Collections.binarySearch(this.reqCount, cnt);
		
		if(newIndex < 0) {
			newIndex = -(newIndex+1);
		}
		
		this.reqCount.add(newIndex, cnt);
		this.servers.add(newIndex, server);
		this.serverToIndex.put(id, newIndex);
		
		return;
	}
	
	public Server getServerWithLeastLoad() {
		return this.servers.get(0);
	}
	
	
	
	
}
