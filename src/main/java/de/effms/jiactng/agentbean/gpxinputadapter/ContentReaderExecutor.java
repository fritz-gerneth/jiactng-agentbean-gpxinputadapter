package de.effms.jiactng.agentbean.gpxinputadapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.Socket;
import java.util.concurrent.Executor;

/**
 * Executor Adapter providing a shortcut to schedule ContentReaders easily.
 *
 * This Executor can act as a factor for {@link ContentReader}.
 */
public class ContentReaderExecutor implements Executor {

    private Executor readerThreadPool;

    private InputToMemoryWriterExecutor writerThreadPool;

    private Log log;

    /**
     * @param readerThreadPool Executor to execute the generated ContentReaders
     * @param writerThreadPool Executor to pass to the ContentReaders
     */
    public ContentReaderExecutor(Executor readerThreadPool, InputToMemoryWriterExecutor writerThreadPool) {
        this.readerThreadPool = readerThreadPool;
        this.writerThreadPool = writerThreadPool;
        this.log = LogFactory.getLog(ContentReaderExecutor.class);
    }

    /**
     * Execute a new ContentReader using the given Socket.
     *
     * @param socket Socket to pass to the ContentReader
     */
    public void execute(Socket socket) {
        this.log.debug("Dispatching ContentReader for socket " + socket.getInetAddress());
        this.execute(new ContentReader(this.writerThreadPool, socket));
    }

    @Override
    public void execute(Runnable command) {
        this.readerThreadPool.execute(command);
    }
}
