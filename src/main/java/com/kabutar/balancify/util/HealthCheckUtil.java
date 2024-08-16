package com.kabutar.balancify.util;

import com.kabutar.balancify.config.Server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HealthCheckUtil {
    public boolean checkHealth(Server server) throws IOException {
        try{
            String host = UrlUtil.getHostName(server);
            String endPoint = server.getHealth().getPath();

            URL url = new URL(host+endPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();
            connection.getResponseCode();

            return connection.getResponseCode() < 400;
        }catch (IOException e){
        	e.printStackTrace();
            return false;
        }
    }
}
