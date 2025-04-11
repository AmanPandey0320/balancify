package com.kabutar.balancify.provider;

import com.kabutar.balancify.config.Route;
import com.kabutar.balancify.constants.AppLevel;
import com.kabutar.balancify.constants.SchedulerType;
import com.kabutar.balancify.config.BaseConfig;
import com.kabutar.balancify.handler.IngressHandler;
import com.kabutar.balancify.util.ResolverUtil;
import com.kabutar.balancify.workers.LoadMonitor;
import com.sun.net.httpserver.HttpServer;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

public class Context {
    private BaseConfig baseConfig;
    private Scheduler scheduler;
    private Proxy proxy;
    private LoadMonitor loadMonitor;

    public Context() throws Exception {
    	
    	this.proxy = new Proxy();
    	this.loadMonitor = new LoadMonitor();
    	
        this.fetchConfigs();
        this.configureScheduler();
        this.initContext();
    }

    public BaseConfig getBaseConfig() {
        return baseConfig;
    }

    /**
     * @description attaches scheduler to the route
     * @throws Exception
     * 
     *TODO: configure different algorithms for different routes (phase-3)
     */
    private void configureScheduler() throws Exception {
        SchedulerType schedulerType = ResolverUtil.resolveScheduler(this.baseConfig.getAlgo());
        boolean isWeighted = (this.baseConfig.getType().equals("W"));
        
        this.scheduler = new Scheduler(
        		schedulerType,
        		isWeighted,
        		this.baseConfig.getHealthCheck().getIntervals(),
        		this.baseConfig.getMaxPoolSize(),
        		this.loadMonitor
        		);

        for(Route route: this.baseConfig.getRoute()){
            this.scheduler.assignScheduler(route.getPath(),route.getServers());
        }
    }
    
    private void initContext() {
    	this.scheduler.init();
    }


    /*
    *
    * */
    private void fetchConfigs() throws FileNotFoundException {
        try{

            InputStream inputStream = new FileInputStream(AppLevel.BASE_CONFIG_FILE_PATH);
            Yaml yaml = new Yaml();
            this.baseConfig = yaml.loadAs(inputStream,BaseConfig.class);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("The path: " + AppLevel.BASE_CONFIG_FILE_PATH+" does not exists");
        }
    }

    public void setServerContext(HttpServer httpServer) throws Exception {
        IngressHandler ingressHandler = new IngressHandler(this.scheduler,this.proxy);
        httpServer.createContext("/", ingressHandler);
    }
}
