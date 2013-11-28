package de.effms.jiactng.agentbean.gpxinputadapter;

import de.dailab.jiactng.agentcore.knowledge.IMemory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.Unmarshaller;
import java.util.concurrent.Executor;

/**
 * Executor Adapter providing a shortcut to schedule InputToMemoryWriters easily.
 *
 * This Executor can act as a factory for {@link InputToMemoryWriter}.
 */
public class InputToMemoryWriterExecutor implements Executor {

    private Executor threadPool;

    private IMemory memory;

    private Unmarshaller unmarshaller;

    private Log log;

    /**
     * @param threadPool Configured Executor to use
     * @param memory Memory to pass to InputToMemoryWriter
     * @param unmarshaller Unmarshaller to pass to InputToMemoryWriter
     */
    public InputToMemoryWriterExecutor(Executor threadPool, IMemory memory, Unmarshaller unmarshaller) {
        this.threadPool = threadPool;
        this.memory = memory;
        this.unmarshaller = unmarshaller;
        this.log = LogFactory.getLog(InputToMemoryWriterExecutor.class);
    }

    /**
     * Execute a new InputToMemoryWriter with the given content
     *
     * @see InputToMemoryWriter
     *
     * @param content The content to pass along
     */
    public void execute(String content) {
        this.log.debug("Dispatching InputToMemoryWriter");
        this.execute(new InputToMemoryWriter(content, this.memory, this.unmarshaller));
    }

    @Override
    public void execute(Runnable command) {
        this.threadPool.execute(command);
    }
}
