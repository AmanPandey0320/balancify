package com.kabutar.balancify.scheduler.dynamic;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.scheduler.rigid.ConsistantHashScheduler;
import com.kabutar.balancify.workers.HealthCheck;
import com.sun.net.httpserver.HttpExchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ResourceBasedConsistentHashScheduler extends ConsistantHashScheduler {

    private double scalingFactor;
    private static final Logger logger = LogManager.getLogger(ResourceBasedConsistentHashScheduler.class);

    public ResourceBasedConsistentHashScheduler(
            ArrayList<Server> servers,
            HealthCheck healthCheck,
            int maxPoolSize,
            double scalingFactor
    ) {
        super(servers, healthCheck, maxPoolSize);
        this.scalingFactor = scalingFactor;
    }

    /**
     *
     * @description return number of virtual nodes on hash ring
     * Log2 on CPU: Handles the diminishing returns of adding many cores.
     * Sqrt on Memory: Prevents one server with tons of RAM from dominating.
     * Scalability: scalingFactor helps fine-tune the number of virtual nodes.
     *
     * @param size
     * @return
     */
    private int getVirtualNodeCount(Server.Size size){
        double cpuScore = (Math.log(size.getCpu() + 1.0) / Math.log(2));
        double memoryScore = Math.sqrt(size.getMemory());

        return  (int) Math.round((cpuScore + memoryScore) * scalingFactor);
    }


    @Override
    public void onServerUpEventHandler(Server server) {
        try{
            int noOfNodes = this.getVirtualNodeCount(server.getSize());
            

            String newId;
            for(int i=0;i<noOfNodes;i++){
                newId = server.getId() + "_node_" + Integer.toString(i);
                server.setId(newId);
                this.addServer(server);
            }
        }catch (Exception e){
        	logger.error(e.getMessage());
        	logger.debug(e.getStackTrace().toString());
        }
    }

    @Override
    public void onServerDownEventHandler(Server server) {
    	try {
    		int noOfNodes = this.getVirtualNodeCount(server.getSize());
        	
        	String newId;
        	for(int i=0;i<noOfNodes;i++){
                newId = server.getId() + "_node_" + Integer.toString(i);
                server.setId(newId);
                this.removeServer(server);
            }
    	}catch(Exception e) {
    		
    	}
    	
    }

	@Override
	public Server schedule(HttpExchange exchange) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		return super.schedule(exchange);
	}
    
    
}
