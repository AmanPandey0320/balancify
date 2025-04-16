package com.kabutar.balancify.scheduler.dynamic;

import com.kabutar.balancify.config.Server;
import com.kabutar.balancify.scheduler.BaseScheduler;
import com.kabutar.balancify.workers.HealthCheck;
import com.kabutar.balancify.workers.LoadMonitor;
import com.sun.net.httpserver.HttpExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class LeastConnectionScheduler extends BaseScheduler {
    private LoadMonitor loadMonitor;
    private static Logger logger = LogManager.getLogger(LeastConnectionScheduler.class);

    public LeastConnectionScheduler(
            ArrayList<Server> servers,
            LoadMonitor loadMonitor
    ) {
        super(servers);
        logger.info("Initializing a least connection connection scheduler");
        this.loadMonitor = loadMonitor;
    }

    @Override
    public Server schedule(HttpExchange exchange) throws Exception {
        return this.loadMonitor.getServerWithLeastLoad();
    }

    @Override
    public void initializeParameters() {
        super.initializeParameters();
    }
}
