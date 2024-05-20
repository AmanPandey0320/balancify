package com.kabutar.balancify;


import com.kabutar.balancify.provider.Context;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class BalancifyApplication {

	public static void main(String[] args) throws IOException {
		int serverPort = 8084;
		HttpServer httpServer = HttpServer.create(new InetSocketAddress(serverPort),0);
		Context context = new Context(httpServer);

		context.setServerContext();

		httpServer.setExecutor(null);
		httpServer.start();
	}

}
