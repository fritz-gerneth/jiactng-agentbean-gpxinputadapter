package de.effms.jiactng.agentbean.gpxinputadapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Runnable to read all contents from a socket and schedule the parsing and writing.
 */
public class ContentReader implements Runnable {

    private InputToMemoryWriterExecutor writeThreadPool;

    private Socket socket;

    private Log log;

    public ContentReader(InputToMemoryWriterExecutor writeThreadPool, Socket socket) {
        this(writeThreadPool, socket, LogFactory.getLog(ContentReader.class));
    }

    public ContentReader(InputToMemoryWriterExecutor writeThreadPool, Socket socket, Log log) {
        this.writeThreadPool = writeThreadPool;
        this.socket = socket;
        this.log = log;
    }

    /**
     * Execute this Runnable.
     *
     * Reads all input from the socket, schedules a {@link InputToMemoryWriter} at the {@code InputToMemoryWriterExecutor}
     * to parse it. The socket is closed afterwards.
     *
     * Exceptions are caught and logged.
     */
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {
            StringBuilder result = new StringBuilder();

            String input;
            while (null != (input = reader.readLine())) {
                result.append(input);
            }

            this.log.info("Finished reading " + result.length() + " characters. Closing socket and scheduling parsing.");
            this.log.debug("Contents: " + result.toString());
            this.writeThreadPool.execute(result.toString());
            this.socket.close();
        } catch (IOException ex) {
            this.log.error(ex);
        }
    }
}
