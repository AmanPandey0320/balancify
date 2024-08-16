package com.kabutar.balancify.scheduler;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.util.HealthCheckUtil;

import java.io.IOException;
import java.util.ArrayList;

public class BaseScheduler {
    private ArrayList<Server> servers;
    private int noOfServer;
    private HealthCheckUtil healthCheckUtil;

    public BaseScheduler(ArrayList<Server> servers) {
        this.servers = servers;
        this.healthCheckUtil = new HealthCheckUtil();
        this.noOfServer = servers.size();
    }

    public void initializeParameters(){}

    public Server schedule() throws IOException {
    	System.out.println("here");
        int idx = 0;
        int cnt;
        int threshHold;
        Server server;
        while(idx < this.noOfServer){
            server = this.servers.get(idx);

            if(healthCheckUtil.checkHealth(server)){
                return server;
            }
            idx++;
        }

        return null;
    }
}
