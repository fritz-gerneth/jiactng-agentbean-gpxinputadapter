package de.effms.jiactng.agentbean.gpxinputadapter;

import de.dailab.jiactng.agentcore.AbstractAgentBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;

/**
 * Entry Point for this AgentBean.
 *
 * Executes the listener in the init lifecycle phase. The Executor is not being shut down by this AgentBean.
 */
public class GpxInputAdapterAgentBean extends AbstractAgentBean {

    private final ExecutorService threadPool;

    private final IncomingConnectionListener listener;

    private final Log log = LogFactory.getLog(GpxInputAdapterAgentBean.class);

    public GpxInputAdapterAgentBean(ExecutorService threadPool, IncomingConnectionListener listener) {
        this.threadPool = threadPool;
        this.listener = listener;
    }

    @Override
    public void doInit() {
        this.log.debug("Scheduling IncomingConnectionListener");
        this.threadPool.execute(this.listener);
        this.setExecuteInterval(-1);
    }
}
