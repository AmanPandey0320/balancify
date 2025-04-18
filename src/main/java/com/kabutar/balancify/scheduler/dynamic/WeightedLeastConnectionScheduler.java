package com.kabutar.balancify.scheduler.dynamic;

import java.util.ArrayList;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.workers.LoadMonitor;

public class WeightedLeastConnectionScheduler extends LeastConnectionScheduler {
	
	private double scalingFactor;
	public WeightedLeastConnectionScheduler(ArrayList<Server> servers, LoadMonitor loadMonitor, double scalingFactor) {
		super(servers, loadMonitor);
		
		this.scalingFactor = scalingFactor;
	}
}
