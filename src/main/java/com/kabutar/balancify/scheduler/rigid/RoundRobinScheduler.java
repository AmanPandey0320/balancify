package com.kabutar.balancify.scheduler.rigid;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.scheduler.BaseScheduler;
import com.kabutar.balancify.workers.HealthCheck;

import java.io.IOException;
import java.util.*;
import com.sun.net.httpserver.HttpExchange;

/*
*
* @class
* @author Aman Kr Pandey, Github: AmanPandey0320
*
* @description In this Round robin, the requests
* will be treated as resource and servers as task
* So if a server reaches a certain threshold
* percentage of usage, it will be queued back and
* other servers can be allocated the requests and
* with in that time the initial server will
* have requests already executed and ready for our use
* This works as Weighted Round Robin if isWeighted flag
* is set as true while creating the object;
*
* */
public class RoundRobinScheduler extends BaseScheduler {
    private ArrayList<Server> servers;
    private HashMap<String,Integer> serverReqCount;
    private Queue<Server> serverQueue;
    private int noOfServer;
    private HealthCheck healthCheck;
    private boolean isWeighted;

    public RoundRobinScheduler(ArrayList<Server> servers, boolean isWeighted, HealthCheck healthCheck) {
        super(servers);
        this.servers = servers;
        this.isWeighted = isWeighted;
        this.noOfServer = servers.size();
        this.healthCheck = healthCheck;
        this.sortServersBySize();
    }

    @Override
    public void initializeParameters(){
        this.serverReqCount = new HashMap<>();
        this.serverQueue = new LinkedList<>();

        for(Server server:this.servers){
            this.serverQueue.add(server);
            this.serverReqCount.put(server.getId(),0);
        }
    }

    @Override
    public Server schedule(HttpExchange exchange) throws IOException {
        int idx = 0;
        Server server;
        while(idx < this.noOfServer){
            server = this.serverQueue.peek();
            assert server != null;
            if(!healthCheck.isServerHealthy(server.getId())){
               //server is unhealthy
                this.serverQueue.remove();
                this.serverQueue.add(server);
            }else{
            	//server is healthy
            	
            	//update load
            	this.updateLoad(server);
            	
            	//check threshold limit
                if(!this.isWeighted || this.isThreshold(server)){
                    this.serverQueue.remove();
                    this.serverQueue.add(server);
                }
                return server;
            }
            idx++;
        }

        return null;
    }
    
    //TODO: to be replaced with server load monitor worker service
    private void updateLoad(Server server) {
    	int cnt = this.serverReqCount.get(server.getId());
    	this.serverReqCount.put(server.getId(),cnt+1);
    }
    
    private boolean isThreshold(Server server) {
    	int cnt = this.serverReqCount.get(server.getId());
    	int threshHold = server.getSize().getCpu();
    	
    	return (cnt >= (threshHold*9)/10);
    	
    }

    private void sortServersBySize(){

        this.servers.sort((a,b) -> {
            if(a.getSize().getCpu() > b.getSize().getCpu()){
                return -1;
            } else if(a.getSize().getCpu() < b.getSize().getCpu()){
                return 1;
            }else{
                if(a.getSize().getMemory() > b.getSize().getMemory()){
                    return -1;
                }else if(a.getSize().getMemory() < b.getSize().getMemory()){
                    return 1;
                }else{
                    return 0;
                }
            }
        });
    }
}
