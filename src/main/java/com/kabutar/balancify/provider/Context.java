package com.kabutar.balancify.provider;

import com.kabutar.balancify.BalancifyApplication;
import com.kabutar.balancify.Constant.AppLevel;
import com.kabutar.balancify.config.BaseConfig;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Objects;

public class Context {
    private BaseConfig baseConfig;

    public Context() throws FileNotFoundException {
        this.fetchConfigs();
    }



    private void fetchConfigs() throws FileNotFoundException {
        try{

            InputStream inputStream = new FileInputStream(AppLevel.BASE_CONFIG_FILE_PATH);
            Yaml yaml = new Yaml();
            this.baseConfig = yaml.loadAs(inputStream,BaseConfig.class);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("The path: " + AppLevel.BASE_CONFIG_FILE_PATH+" does not exists");
        }
    }

    public BaseConfig getBaseConfig() {
        return baseConfig;
    }

    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    public void setServerContext(HttpServer httpServer){
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
