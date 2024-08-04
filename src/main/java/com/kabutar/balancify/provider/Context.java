package com.kabutar.balancify.provider;

import com.kabutar.balancify.config.Route;
import com.kabutar.balancify.constants.AppLevel;
import com.kabutar.balancify.config.BaseConfig;
import com.kabutar.balancify.handler.IngressHandler;
import com.kabutar.balancify.scheduler.SchedulerType;
import com.kabutar.balancify.util.ResolverUtil;
import com.sun.net.httpserver.HttpServer;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

public class Context {
    private BaseConfig baseConfig;
    private Scheduler scheduler;

    public Context() throws Exception {
        this.fetchConfigs();
        this.configureScheduler();
    }

    public BaseConfig getBaseConfig() {
        return baseConfig;
    }

    /*
    *
    * */
    private void configureScheduler() throws Exception {
        SchedulerType schedulerType = ResolverUtil.resolveScheduler(this.baseConfig.getAlgo());
        this.scheduler = new Scheduler(schedulerType);

        for(Route route: this.baseConfig.getRoute()){
            this.scheduler.init(route.getPath(),route.getServers());
        }
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
        IngressHandler requestHandler = new IngressHandler(this.scheduler);
        httpServer.createContext("/", requestHandler);
    }
}
