package com.cafiaso.server.network;

import com.cafiaso.server.network.server.NetworkServer;
import com.cafiaso.server.network.server.SocketServer;
import com.google.inject.AbstractModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NetworkServer.class).to(SocketServer.class);
        bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
    }
}
