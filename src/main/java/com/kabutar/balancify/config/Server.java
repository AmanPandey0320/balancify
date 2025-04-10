package com.kabutar.balancify.config;

public class Server {
    private String id;
	private BaseConfig.Health health;
    private Size size;
    private String url;


    // Getters and Setters
    
    

    public String getId() {
        return id;
    }

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		return "Server [id=" + id + ", health=" + health + ", size=" + size + ", url=" + url + "]";
	}

	
    
    
}
