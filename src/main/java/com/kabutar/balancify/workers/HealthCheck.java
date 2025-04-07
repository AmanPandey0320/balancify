package com.kabutar.balancify.workers;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.event.HealthCheckEvent;
import com.kabutar.balancify.util.UrlUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HealthCheck {
	HashMap<String,Boolean> serverHealthMap;
	HashMap<String,Server> serverMap;
	HashMap<String,HealthCheckEvent> serverEvent;
			
	
	
	
	public HealthCheck() {
		super();
		this.serverEvent = new HashMap<>();
		this.serverHealthMap = new HashMap<>();
		this.serverMap = new HashMap<>();
		
	}

	public void addServer(ArrayList<Server> servers) {
		for(Server server:servers) {
			serverHealthMap.put(server.getId(), false);
			serverMap.put(server.getId(), server);
		}
	}
	
	public boolean isServerHealthy(String serverId) {
		if(this.serverHealthMap.containsKey(serverId)) {
			return this.serverHealthMap.get(serverId);
		}
		return false;
	}
	
	/**
	 * 
	 * @param servers
	 * @param trigger
	 */
	public void registerEventListner(HealthCheckEvent trigger) {
		for(Server server:trigger.getServerList()) {
			this.serverEvent.put(server.getId(), trigger);
		}
	}
	
	public void initSchedule(int interval) {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		scheduler.scheduleAtFixedRate(()->{
			boolean currentServerHealth;
			for(Entry<String,Server> entry:this.serverMap.entrySet()) {
				currentServerHealth = this.checkServerHealth(entry.getValue());
				if(currentServerHealth && !this.serverHealthMap.get(entry.getKey())) {
					//server up now but was down earlier
					this.serverEvent.get(entry.getKey()).onServerUpEventHandler(entry.getValue());
				}
				
				if(!currentServerHealth && this.serverHealthMap.get(entry.getKey())) {
					this.serverEvent.get(entry.getKey()).onServerDownEventHandler(entry.getValue());
				}
				
				this.serverHealthMap.put(entry.getKey(),currentServerHealth);
			}
			System.out.println("healthy servers: "+this.serverHealthMap);
		}, 0, interval, TimeUnit.SECONDS);
		
	}
	
    private boolean checkServerHealth(Server server) {
        try{
            String host = UrlUtil.getHostName(server);
            String endPoint = server.getHealth().getPath();

            URL url = new URL(host+endPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();
            connection.getResponseCode();

            return connection.getResponseCode() < 400;
        }catch (IOException e){
        	System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
}
