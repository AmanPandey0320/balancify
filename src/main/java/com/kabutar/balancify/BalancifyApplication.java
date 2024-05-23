package com.kabutar.balancify;


import com.kabutar.balancify.config.BaseConfig;
import com.kabutar.balancify.provider.Context;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class BalancifyApplication {

	public static void main(String[] args) throws Exception {
		Context context = new Context();
		BaseConfig config = context.getBaseConfig();
		HttpServer httpServer = HttpServer.create(new InetSocketAddress(config.getPort()),0);

		context.setServerContext(httpServer);

		httpServer.setExecutor(null);
		httpServer.start();
	}

}
