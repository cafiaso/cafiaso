package com.cafiaso.server;

import com.cafiaso.server.configuration.PropertiesServerConfiguration;
import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.network.NetworkModule;
import com.google.inject.AbstractModule;

public class ServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Server.class).to(ServerImpl.class);
        bind(ServerConfiguration.class).to(PropertiesServerConfiguration.class).asEagerSingleton();

        install(new NetworkModule());
    }
}
