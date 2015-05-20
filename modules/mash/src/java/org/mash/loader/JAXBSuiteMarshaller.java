package org.mash.loader;

import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Manage the JAXBContext and abstract the JAXB interfaces from the rest of the application.
 * <p/>
 * Other types of xml marshallers are possible, allowing users to not use JAXB should they so choose.  Different
 * implementations need to work with the supplied objects in the xml package however, as that's what the framework works
 * with.
 * <p/>
 *  Date: Jul 1, 2009 Time: 11:13:09 AM
 */
public class JAXBSuiteMarshaller implements SuiteMarshaller
{
    private static final Logger log = Logger.getLogger(JAXBSuiteMarshaller.class.getName());
    private static JAXBContext jc;
    private String contextPath;

    public JAXBSuiteMarshaller(String contextPath)
    {
        this.contextPath = contextPath;
    }

    public JAXBSuiteMarshaller()
    {
        this("org.mash.config");
    }

    /**
     * Marshal the submitted object to an XML string
     *
     * @param element defined in the xml package
     * @return XML as string
     * @throws org.mash.loader.SuiteMarshallerException
     *          when marshalling throws a JAXBException
     */
    public String marshal(Object element) throws SuiteMarshallerException
    {
        String outboundXML = null;
        log.debug("Within marshal");

        if (element != null)
        {

            StringWriter writer = new StringWriter(2000);
            try
            {
                Marshaller u = createContext().createMarshaller();
                u.marshal(element, writer);
                outboundXML = writer.getBuffer().toString();
            }
            catch (JAXBException e)
            {
                throw new SuiteMarshallerException("Received JAXB exception while marshaling.", e);
            }
        }
        return outboundXML;
    }

    /**
     * Unmarshal the xml and create the appropriate JAXB objects defined in the xml package
     *
     * @param xml as string
     * @return jaxb object
     * @throws org.mash.loader.SuiteMarshallerException
     *          when unmarshalling throws UnmarshalException or JAXBException
     */
    public Object unmarshal(String xml) throws SuiteMarshallerException
    {
        Object result;
        try
        {
            Unmarshaller u = createContext().createUnmarshaller();
            StringReader xmlTextString = new StringReader(xml);
            Object object = u.unmarshal(xmlTextString);
            if (object instanceof JAXBElement)
            {
                result = ((JAXBElement) object).getValue();
            }
            else
            {
                result = object;
            }
        }
        catch (Exception e)
        {
            throw new SuiteMarshallerException("Received an exception when unmarshaling the message.", e);
        }
        return result;
    }

    protected JAXBContext createContext() throws JAXBException
    {
        if (jc == null)
        {
            jc = JAXBContext.newInstance(contextPath);
        }
        return jc;
    }

}
