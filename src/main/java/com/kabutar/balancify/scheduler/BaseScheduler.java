package com.kabutar.balancify.scheduler;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.workers.HealthCheck;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import com.sun.net.httpserver.HttpExchange;

public class BaseScheduler {
    private ArrayList<Server> servers;
    private int noOfServer;
    private HealthCheck healthCheck;

    public BaseScheduler(ArrayList<Server> servers) {
        this.servers = servers;
        this.healthCheck = new HealthCheck();
        this.noOfServer = servers.size();
    }

    public void initializeParameters(){}

    public Server schedule(HttpExchange exchange) throws IOException, NoSuchAlgorithmException {
        int idx = 0;
        int cnt;
        int threshHold;
        Server server;
        while(idx < this.noOfServer){
            server = this.servers.get(idx);

            if(healthCheck.isServerHealthy(server.getId())){
                return server;
            }
            idx++;
        }

        return null;
    }
}
