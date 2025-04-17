package com.kabutar.balancify;


import com.kabutar.balancify.config.BaseConfig;
import com.kabutar.balancify.provider.Context;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BalancifyApplication {

	public static void main(String[] args) throws Exception {
		Logger logger  = LogManager.getLogger(BalancifyApplication.class);
		Context context = new Context();
		BaseConfig config = context.getBaseConfig();
		HttpServer httpServer = HttpServer.create(new InetSocketAddress(config.getPort()),0);

		context.setServerContext(httpServer);
		
		logger.info("Server started at port: {}",config.getPort());

		httpServer.setExecutor(null);
		httpServer.start();
	}

}
