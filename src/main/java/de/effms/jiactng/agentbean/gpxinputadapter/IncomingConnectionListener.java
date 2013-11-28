package de.effms.jiactng.agentbean.gpxinputadapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Runnable to listen to incoming connections and dispatching the handling using a ContentReader.
 */
public class IncomingConnectionListener implements Runnable {

    private ServerSocket socket;

    private ContentReaderExecutor readerThreadPool;

    private final Log log;

    /**
     * @param socket ServerSocket to listen on
     * @param readerThreadPool ThreadPool to dispatch new connections to
     */
    public IncomingConnectionListener(ServerSocket socket, ContentReaderExecutor readerThreadPool) {
        this(socket, readerThreadPool, LogFactory.getLog(IncomingConnectionListener.class));
    }

    /**
     * @param socket ServerSocket to listen on
     * @param readerThreadPool ThreadPool to dispatch new connections to
     * @param log Log to use
     */
    public IncomingConnectionListener(ServerSocket socket, ContentReaderExecutor readerThreadPool, Log log) {
        this.socket = socket;
        this.readerThreadPool = readerThreadPool;
        this.log = log;
    }

    /**
     * Execute this runnable.
     *
     * Will run until it's thread is interrupted.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = this.socket.accept();
                this.log.info("Accepted connection from " + socket.getInetAddress());
                this.readerThreadPool.execute(socket);
            } catch (IOException ex) {
                this.log.warn(ex);
            }
        }
    }
}
