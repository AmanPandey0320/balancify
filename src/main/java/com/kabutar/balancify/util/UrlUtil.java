package com.kabutar.balancify.util;

import com.kabutar.balancify.config.Server;

public class UrlUtil {
	public static String getHostName(Server server) {
    	StringBuilder builder = new StringBuilder();
    	String protocol = server.getProtocol();
    	String host = server.getIp();
    	String port = server.getPort();
    	
    	builder.append(protocol);
    	builder.append("://");
    	builder.append(host);
    	builder.append(":");
    	builder.append(port);
    	
    	return builder.toString();
    	
    }
}
