package com.kabutar.balancify.util;

import com.kabutar.balancify.config.Server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HealthCheckUtil {
	HashMap<String,Boolean> serverHealthMap = new HashMap<>();
	HashMap<String,Server> serverMap = new HashMap<>();
	int timer = 0;
	
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
	
	public void initSchedule(int interval) {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(()->{
			System.out.println("timer: "+timer);
			this.timer++;
		}, 0, interval, TimeUnit.SECONDS);
		
	}
	
    private boolean checkServerHealth(Server server) throws IOException {
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
        	e.printStackTrace();
            return false;
        }
    }
}
