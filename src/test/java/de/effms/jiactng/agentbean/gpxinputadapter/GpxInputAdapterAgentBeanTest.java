package de.effms.jiactng.agentbean.gpxinputadapter;

import mockit.Mocked;
import mockit.Verifications;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

public class GpxInputAdapterAgentBeanTest {

    @Mocked
    private IncomingConnectionListener listener;

    @Mocked
    private ExecutorService threadPool;

    private GpxInputAdapterAgentBean inputAdapter;

    @Before
    public void setUp() {
        this.inputAdapter = new GpxInputAdapterAgentBean(this.threadPool, this.listener);
    }

    @Test
    public void executesListenerOnExecutePhase() {
        this.inputAdapter.doInit();

        new Verifications() {{
            GpxInputAdapterAgentBeanTest.this.threadPool.execute(GpxInputAdapterAgentBeanTest.this.listener); times = 1;
        }};
    }
}
