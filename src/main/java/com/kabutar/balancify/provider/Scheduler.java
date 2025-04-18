package com.kabutar.balancify.provider;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.constants.SchedulerType;
import com.kabutar.balancify.scheduler.BaseScheduler;
import com.kabutar.balancify.scheduler.dynamic.LeastConnectionScheduler;
import com.kabutar.balancify.scheduler.dynamic.ResourceBasedConsistentHashScheduler;
import com.kabutar.balancify.scheduler.dynamic.WeightedLeastConnectionScheduler;
import com.kabutar.balancify.scheduler.rigid.ConsistantHashScheduler;
import com.kabutar.balancify.scheduler.rigid.LinearHashScheduler;
import com.kabutar.balancify.scheduler.rigid.RoundRobinScheduler;
import com.kabutar.balancify.workers.HealthCheck;
import com.kabutar.balancify.workers.LoadMonitor;

import java.util.ArrayList;
import java.util.HashMap;
import com.sun.net.httpserver.HttpExchange;

public class Scheduler {
    private final SchedulerType schedulerType;
    private final boolean isWeighted;
    private HashMap<String, BaseScheduler> schedulers;
    private HealthCheck healthCheck;
    private int healthCheckInterval = 30;
    private int maxServerPoolSize = 32;
    private LoadMonitor loadMonitor;
    private double scalingFactor;

    public Scheduler(
    		SchedulerType schedulerType, 
    		boolean isWeighted, 
    		int healthCheckInterval,
    		int maxServerPoolSize,
    		LoadMonitor loadMonitor,
    		double scalingFactor
    		) {
    	this.schedulerType = schedulerType;
        this.isWeighted = isWeighted;
        this.schedulers = new HashMap<>();
        this.healthCheck = new HealthCheck();
        this.healthCheckInterval = healthCheckInterval;
        this.maxServerPoolSize = maxServerPoolSize;
        this.loadMonitor = loadMonitor;
        this.scalingFactor = scalingFactor;
	}

	public void assignScheduler(String path, ArrayList<Server> servers){
        BaseScheduler scheduler = null;
        if(schedulerType == SchedulerType.ROUND_ROBIN){
            scheduler = new RoundRobinScheduler(servers,this.isWeighted,this.healthCheck);
        }else if(schedulerType == SchedulerType.LINEAR_HASH) {
        	scheduler = new LinearHashScheduler(servers,this.healthCheck);
        }else if(schedulerType == SchedulerType.CONSISTANT_HASH) {
        	if(this.isWeighted) {
        		scheduler = new ResourceBasedConsistentHashScheduler(servers,this.healthCheck,this.maxServerPoolSize,this.scalingFactor);
        	}else {
        		scheduler = new ConsistantHashScheduler(servers,this.healthCheck,this.maxServerPoolSize);
        	}
        }else if(schedulerType == SchedulerType.LEAST_CONNECTION){
            if(this.isWeighted) {
            	scheduler = new WeightedLeastConnectionScheduler(servers,this.loadMonitor,this.scalingFactor);
            }else {
            	scheduler = new LeastConnectionScheduler(servers,this.loadMonitor);
            }
        }
        
        if(scheduler != null) {
        	scheduler.initializeParameters();
        	this.healthCheck.addServer(servers);
        	this.schedulers.put(path,scheduler);
        	this.loadMonitor.addServers(servers);
        }
        

    }
	
	public void init() {
		this.healthCheck.initSchedule(healthCheckInterval);
	}

    public Server getServeFromPool(HttpExchange exchange) throws Exception {
    	String path = exchange.getRequestURI().getPath();
        BaseScheduler scheduler = null;

        if(!this.schedulers.containsKey(path)){
            throw new Exception("The path :"+path+" is not recognized");
        }

        scheduler = this.schedulers.get(path);
        

        return scheduler.schedule(exchange);
    }
}
