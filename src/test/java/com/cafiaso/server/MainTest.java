package com.cafiaso.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

class MainTest {

    @Test
    void main_shouldStartServerWithDefaults() {
        Server server = mock(Server.class);

        Injector injector = mock(Injector.class);
        when(injector.getInstance(Server.class)).thenReturn(server);

        try (MockedStatic<Guice> guiceMock = mockStatic(Guice.class)) {
            guiceMock.when(() -> Guice.createInjector(any(Module[].class))).thenReturn(injector);

            Main.main(new String[]{});

            verify(server).start("localhost", 25565);
        }
    }

    @Test
    void main_shouldStartServerWithCustomHostAndPort() {
        Server server = mock(Server.class);

        Injector injector = mock(Injector.class);
        when(injector.getInstance(Server.class)).thenReturn(server);

        try (MockedStatic<Guice> guiceMock = mockStatic(Guice.class)) {
            guiceMock.when(() -> Guice.createInjector(any(Module[].class))).thenReturn(injector);

            Main.main(new String[]{"--host", "999.999.999.999", "--port", "9999"});

            verify(server).start("999.999.999.999", 9999);
        }
    }
}
