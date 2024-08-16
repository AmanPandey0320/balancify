package com.kabutar.balancify.config;

public class Server {
    private String id;
    private String protocol;
    private String ip;
    private String port;
	private BaseConfig.Health health;
    private Size size;
    
    
    public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}


    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
		return "Server [id=" + id + ", protocol=" + protocol + ", ip=" + ip + ", port=" + port + ", health=" + health
				+ ", size=" + size + "]";
	}
    
    
}
