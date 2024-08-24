package com.kabutar.balancify.scheduler.rigid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.scheduler.BaseScheduler;
import com.kabutar.balancify.util.HealthCheckUtil;

/**
 * 
 * @author Aman Kr Pandey
 *
 */
public class LinearHashScheduler extends BaseScheduler {
	private ArrayList<Server> servers;
    private HealthCheckUtil healthCheckUtil;
    private Map<Integer,Integer> servermap;
    private int noOfReq;
    

	public LinearHashScheduler(ArrayList<Server> servers) {
		super(servers);
		this.noOfReq = 0;
		this.servers = servers;
	}
	
	private void prepareServerHash() {
		//TODO
	}
	
}
