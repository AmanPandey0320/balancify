package com.kabutar.balancify.config;

public class Server {
    private String id;
    private String hostname;
    private BaseConfig.Health health;
    private Size size;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public BaseConfig.Health getHealth() {
        return health;
    }

    public void setHealth(BaseConfig.Health health) {
        this.health = health;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public static class Size {
        private int cpu;
        private int memory;

        // Getters and Setters

        public int getCpu() {
            return cpu;
        }

        public void setCpu(int cpu) {
            this.cpu = cpu;
        }

        public int getMemory() {
            return memory;
        }

        public void setMemory(int memory) {
            this.memory = memory;
        }

        @Override
        public String toString() {
            return "Size{" +
                    "cpu=" + cpu +
                    ", memory=" + memory +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Server{" +
                "id='" + id + '\'' +
                ", hostname='" + hostname + '\'' +
                ", health=" + health +
                ", size=" + size +
                '}';
    }
}
