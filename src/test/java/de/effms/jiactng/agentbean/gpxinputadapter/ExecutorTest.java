package de.effms.jiactng.agentbean.gpxinputadapter;

import mockit.Verifications;
import org.junit.Test;

import java.util.concurrent.Executor;

abstract public class ExecutorTest {
    abstract protected Executor getExecutor();

    abstract protected Executor getAdaptedExecutor();

    @Test
    public void delegatesRunnableToAdaptedExecutor(final Runnable runnable) {
        this.getExecutor().execute(runnable);

        new Verifications() {{
            ExecutorTest.this.getAdaptedExecutor().execute(onInstance(runnable));
        }};
    }
}
