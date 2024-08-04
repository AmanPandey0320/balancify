package com.kabutar.balancify.provider;

import com.kabutar.balancify.config.Server;
import com.sun.net.httpserver.HttpExchange;

public class Proxy {
    private Server server;

    public Proxy(Server server) {
        this.server = server;
    }

    public void configure(){

    }
}
