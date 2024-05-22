package com.kabutar.balancify.config;

import java.util.List;

public class Route {
    private String id;
    private String path;
    private List<Server> servers;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", servers=" + servers +
                '}';
    }
}
