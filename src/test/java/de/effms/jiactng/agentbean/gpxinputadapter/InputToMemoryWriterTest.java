package de.effms.jiactng.agentbean.gpxinputadapter;

import de.dailab.jiactng.agentcore.knowledge.IFact;
import de.dailab.jiactng.agentcore.knowledge.IMemory;
import de.effms.jiactng.facts.gpx.GpxType;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.apache.commons.logging.Log;
import org.junit.Test;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class InputToMemoryWriterTest {

    @Mocked
    private IMemory memory;

    @Mocked
    private Log log;

    @Mocked
    private Unmarshaller unmarshaller;

    private InputToMemoryWriter getWriter(String content) {
        return new InputToMemoryWriter(content, this.memory, this.unmarshaller, this.log);
    }

    @Test
    public void writesGpxTypeToMemory(final JAXBElement element, final GpxType gpxType) throws JAXBException{
        new NonStrictExpectations() {{
            InputToMemoryWriterTest.this.unmarshaller.unmarshal((StringReader) any); result = element;
            element.getDeclaredType(); result = GpxType.class;
            element.getValue(); result = gpxType;
        }};

        this.getWriter("").run();

        new Verifications() {{
            InputToMemoryWriterTest.this.memory.write(gpxType); times = 1;
        }};
    }

    @Test
    public void fatalLogOnDeclaredTypeMismatchAndNoMemoryWrite(final JAXBElement element, final GpxType gpxType) throws JAXBException{
        new NonStrictExpectations() {{
            InputToMemoryWriterTest.this.unmarshaller.unmarshal((StringReader) any); result = element;
            element.getDeclaredType(); result = String.class;
        }};

        this.getWriter("").run();

        new Verifications() {{
            InputToMemoryWriterTest.this.log.fatal(anyString);
            InputToMemoryWriterTest.this.memory.write((IFact) any); times = 0;
        }};
    }

    @Test
    public void infoLogOnInputSyntaxError() throws JAXBException{
        new NonStrictExpectations() {{
            InputToMemoryWriterTest.this.unmarshaller.unmarshal((StringReader) any); result = new UnmarshalException(anyString);
        }};

        this.getWriter("").run();

        new Verifications() {{
            InputToMemoryWriterTest.this.log.info(anyString);
            InputToMemoryWriterTest.this.memory.write((IFact) any); times = 0;
        }};
    }

    @Test
    public void errorLogOnOtherJAXBException() throws JAXBException{
        new NonStrictExpectations() {{
            InputToMemoryWriterTest.this.unmarshaller.unmarshal((StringReader) any); result = new JAXBException(anyString);
        }};

        this.getWriter("").run();

        new Verifications() {{
            InputToMemoryWriterTest.this.log.error(anyString);
            InputToMemoryWriterTest.this.memory.write((IFact) any); times = 0;
        }};
    }
}
