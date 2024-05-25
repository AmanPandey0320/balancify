package com.kabutar.balancify.provider;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.scheduler.BaseScheduler;
import com.kabutar.balancify.scheduler.RoundRobinScheduler;
import com.kabutar.balancify.scheduler.SchedulerType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Scheduler {
    private final SchedulerType schedulerType;
    private HashMap<String, BaseScheduler> schedulers;

    public Scheduler(SchedulerType schedulerType) {
        this.schedulerType = schedulerType;
        this.schedulers = new HashMap<>();
    }

    public void init(String path, ArrayList<Server> servers){
        BaseScheduler scheduler = null;
        if(schedulerType == SchedulerType.ROUND_ROBIN){
            scheduler = new RoundRobinScheduler(servers);
            scheduler.initializeParameters();
        }

        this.schedulers.put(path,scheduler);

    }

    public Server getServeFromPool(String path) throws Exception {
        BaseScheduler scheduler = null;

        if(!this.schedulers.containsKey(path)){
            throw new Exception("The path :"+path+" is not recognized");
        }

        scheduler = this.schedulers.get(path);

        return scheduler.schedule();
    }
}
