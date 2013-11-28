package de.effms.jiactng.agentbean.gpxinputadapter;

import mockit.Mocked;
import mockit.Verifications;
import org.junit.Before;
import org.junit.Test;

import java.net.Socket;
import java.util.concurrent.Executor;

public class ContentReaderExecutorTest extends ExecutorTest {

    @Mocked
    private Executor readerThreadPool;

    @Mocked
    private InputToMemoryWriterExecutor writerThreadPool;

    private ContentReaderExecutor executor;

    @Before
    public void setUp() {
        this.executor = new ContentReaderExecutor(this.readerThreadPool, this.writerThreadPool);
    }

    protected Executor getExecutor() {
        return this.executor;
    }

    protected Executor getAdaptedExecutor() {
        return this.readerThreadPool;
    }

    @Test
    public void executesNewContentReader(final Socket socket) {
        this.executor.execute(socket);

        new Verifications() {{
            ContentReaderExecutorTest.this.readerThreadPool.execute(withInstanceOf(ContentReader.class));
        }};
    }
}
