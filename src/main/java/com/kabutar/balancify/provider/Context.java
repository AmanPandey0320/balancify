package com.kabutar.balancify.provider;

import com.kabutar.balancify.constant.AppLevel;
import com.kabutar.balancify.config.BaseConfig;
import com.kabutar.balancify.scheduler.SchedulerType;
import com.kabutar.balancify.util.Resolver;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
        SchedulerType schedulerType = Resolver.resolveScheduler(this.baseConfig.getAlgo());
        this.scheduler = new Scheduler(schedulerType);
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


        httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String path = exchange.getRequestURI().getPath();
                exchange.sendResponseHeaders(200, path.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(path.getBytes());
                output.flush();
                exchange.close();
            }
        });
    }
}
