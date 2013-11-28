package de.effms.jiactng.agentbean.gpxinputadapter;

import de.dailab.jiactng.agentcore.knowledge.IMemory;
import de.effms.jiactng.facts.gpx.GpxType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Runnable to parse a string as GPX 1.1 content into an object tree and save it to the memory.
 *
 * The Object Tree is of type {@link de.effms.jiactng.facts.gpx.GpxType}. The Unmarshaller can be created by
 * <code>
 * JAXBContext context = JAXBContext.newInstance("de.effms.jiactng.facts.gpx");
 * Unmarshaller unmarshaller = context.createUnmarshaller();
 * </code>
 * or by using the {@link UnmarshallerFactory}.
 */
public class InputToMemoryWriter implements Runnable {

    private IMemory memory;

    private String contents;

    private Log log;

    private Unmarshaller unmarshaller;

    /**
     * @param contents Content to parse
     * @param memory Memory to save to
     * @param unmarshaller Unmarshaller to use for the content
     */
    public InputToMemoryWriter(String contents, IMemory memory, Unmarshaller unmarshaller) {
        this(contents, memory, unmarshaller, LogFactory.getLog(InputToMemoryWriter.class));
    }

    /**
     * @param contents Content to parse
     * @param memory Memory to save to
     * @param unmarshaller Unmarshaller to use for the content
     * @param log Log to write to
     */
    public InputToMemoryWriter(String contents, IMemory memory, Unmarshaller unmarshaller, Log log) {
        this.memory = memory;
        this.contents = contents;
        this.unmarshaller = unmarshaller;
        this.log = log;
    }

    /**
     * Run the pre-configured Runnable.
     *
     * Parsing-relevant exceptions are written to the log.
     */
    @Override
    public void run() {
        try {
            JAXBElement result = (JAXBElement) this.unmarshaller.unmarshal(new StringReader(this.contents));
            if (GpxType.class != result.getDeclaredType()) {
                this.log.fatal("Unmarshalling was successfull but returned wrong type " + result.getDeclaredType() + ". Suspecting misconfiguration");
                return;
            }

            this.log.info("Writing new GPX Facts to memory");
            this.memory.write((GpxType) result.getValue());
        } catch (UnmarshalException ex) {
            this.log.info("Input has invalid markup: " + ex.getMessage());
        } catch (JAXBException ex) {
            this.log.error(ex);
        }
    }
}
