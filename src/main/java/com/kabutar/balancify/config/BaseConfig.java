package com.kabutar.balancify.config;

import java.util.List;

public class BaseConfig {
    private String name;
    private int port;
    private String algo;
    private String type;
    private List<Route> route;
    private HealthCheck healthCheck;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public List<Route> getRoute() {
        return route;
    }

    public void setRoute(List<Route> route) {
        this.route = route;
    }

    public HealthCheck getHealthCheck() {
        return healthCheck;
    }

    public void setHealthCheck(HealthCheck healthCheck) {
        this.healthCheck = healthCheck;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "name='" + name + '\'' +
                ", port=" + port +
                ", algo='" + algo + '\'' +
                ", route=" + route +
                ", healthCheck=" + healthCheck +
                ", type=" + type +
                '}';
    }

    public static class Health {
        private String path;

        // Getters and Setters

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return "Health{" +
                    "path='" + path + '\'' +
                    '}';
        }
    }

    public static class HealthCheck {
        private int intervals;

        // Getters and Setters

        public int getIntervals() {
            return intervals;
        }

        public void setIntervals(int intervals) {
            this.intervals = intervals;
        }

        @Override
        public String toString() {
            return "HealthCheck{" +
                    "intervals=" + intervals +
                    '}';
        }
    }
}
