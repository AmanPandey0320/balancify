package com.kabutar.balancify.provider;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.constants.SchedulerType;
import com.kabutar.balancify.scheduler.BaseScheduler;
import com.kabutar.balancify.scheduler.rigid.LinearHashScheduler;
import com.kabutar.balancify.scheduler.rigid.RoundRobinScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import com.sun.net.httpserver.HttpExchange;

public class Scheduler {
    private final SchedulerType schedulerType;
    private final boolean isWeighted;
    private HashMap<String, BaseScheduler> schedulers;

    public Scheduler(SchedulerType schedulerType) {
        this.schedulerType = schedulerType;
        this.isWeighted = false;
        this.schedulers = new HashMap<>();
    }

    public Scheduler(SchedulerType schedulerType, boolean isWeighted) {
    	this.schedulerType = schedulerType;
        this.isWeighted = isWeighted;
        this.schedulers = new HashMap<>();
    	
	}

	public void init(String path, ArrayList<Server> servers){
        BaseScheduler scheduler = null;
        if(schedulerType == SchedulerType.ROUND_ROBIN){
            scheduler = new RoundRobinScheduler(servers,this.isWeighted);
            scheduler.initializeParameters();
        }else if(schedulerType == SchedulerType.LINEAR_HASH) {
        	scheduler = new LinearHashScheduler(servers);
        }

        this.schedulers.put(path,scheduler);

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
