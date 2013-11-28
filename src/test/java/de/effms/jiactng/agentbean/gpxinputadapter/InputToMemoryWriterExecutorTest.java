package de.effms.jiactng.agentbean.gpxinputadapter;

import de.dailab.jiactng.agentcore.knowledge.IMemory;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.Unmarshaller;
import java.util.concurrent.Executor;

public class InputToMemoryWriterExecutorTest extends ExecutorTest {

    @Mocked
    private Executor threadPool;

    @Mocked
    private IMemory memory;

    @Mocked
    private Unmarshaller unmarshaller;

    private InputToMemoryWriterExecutor executor;

    @Before
    public void setUp() {
        this.executor = new InputToMemoryWriterExecutor(this.threadPool, this.memory, this.unmarshaller);
    }

    @Override
    protected Executor getExecutor() {
        return this.executor;
    }

    @Override
    protected Executor getAdaptedExecutor() {
        return this.threadPool;
    }

    @Test
    public void executesNewInputToMemoryWriter() {
        this.executor.execute("");

        new Verifications() {{
            InputToMemoryWriterExecutorTest.this.threadPool.execute(withInstanceOf(InputToMemoryWriter.class));
        }};
    }
}
