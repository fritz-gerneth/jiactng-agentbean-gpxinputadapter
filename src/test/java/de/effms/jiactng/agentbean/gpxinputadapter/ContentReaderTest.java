package de.effms.jiactng.agentbean.gpxinputadapter;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ContentReaderTest {

    @Mocked
    private InputToMemoryWriterExecutor writerThreadPool;

    @Mocked
    private Socket socket;

    @Mocked
    private Log log;

    private ContentReader reader;

    @Before
    public void setUp() {
        this.reader = new ContentReader(this.writerThreadPool, this.socket, this.log);
    }

    @Test
    public void delegatesReadContentToExecutor() throws IOException {
        final InputStream input = IOUtils.toInputStream("some test data for my input stream");

        new NonStrictExpectations() {{
            ContentReaderTest.this.socket.getInputStream(); result = input;
        }};

        this.reader.run();

        new Verifications() {{
            ContentReaderTest.this.writerThreadPool.execute("some test data for my input stream");
        }};
    }

    @Test
    public void socketIsClosed() throws IOException {
        final InputStream input = IOUtils.toInputStream("some test data for my input stream");

        new NonStrictExpectations() {{
            ContentReaderTest.this.socket.getInputStream(); result = input;
        }};

        this.reader.run();

        new Verifications() {{
            ContentReaderTest.this.socket.close(); times = 1;
        }};
    }

    @Test
    public void exceptionsAreLoggedAsError() throws IOException {
        final IOException error = new IOException("Ex");

        new NonStrictExpectations() {{
            ContentReaderTest.this.socket.getInputStream(); result = error;
        }};

        this.reader.run();

        new Verifications() {{
            ContentReaderTest.this.log.error(withSameInstance(error));
        }};
    }
}
