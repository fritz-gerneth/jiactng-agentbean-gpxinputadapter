package de.effms.jiactng.agentbean.gpxinputadapter;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Simple factory to create a ServerSockets on a previously defined port.
 */
public class GpsServerSocketFactory {
    private int port;

    public GpsServerSocketFactory(int port) {
        this.port = port;
    }

    public ServerSocket getInstance() throws IOException {
        return new ServerSocket(this.port);
    }
}
