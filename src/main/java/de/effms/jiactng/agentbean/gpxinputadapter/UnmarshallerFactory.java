package de.effms.jiactng.agentbean.gpxinputadapter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Simple factory to create JAXB Unmarshallers
 */
public class UnmarshallerFactory {

    private String contextPath;

    /**
     * @param contextPath Package name of the classes to use
     */
    public UnmarshallerFactory(String contextPath) {
        this.contextPath = contextPath;
    }

    public Unmarshaller getInstance() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(this.contextPath);
        return context.createUnmarshaller();
    }
}
