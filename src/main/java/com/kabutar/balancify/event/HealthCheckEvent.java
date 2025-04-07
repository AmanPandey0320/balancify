package com.kabutar.balancify.event;

import java.util.ArrayList;

import com.kabutar.balancify.config.Server;

public interface HealthCheckEvent {
	public void onServerUpEventHandler(Server server);
    public void onServerDownEventHandler(Server server);
    public ArrayList<Server> getServerList();
}
