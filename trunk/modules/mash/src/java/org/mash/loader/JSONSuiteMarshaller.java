package org.mash.loader;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamReader;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author teastlack
 * @since Dec 5, 2011 1:26:57 PM
 */
public class JSONSuiteMarshaller extends JAXBSuiteMarshaller
{
    private static final Logger log = Logger.getLogger(JSONSuiteMarshaller.class.getName());
    private Map<String, String> namespaceMappings;

    public JSONSuiteMarshaller()
    {
    }

    public JSONSuiteMarshaller(String contextPath)
    {
        super(contextPath);

    }

    @Override
    public String marshal(Object element) throws SuiteMarshallerException
    {
        String outboundJSON = null;
        log.debug("Marshalling as json");
        if (element != null)
        {
            try
            {
                StringWriter writer = new StringWriter(2000);
                Configuration config = new Configuration();
                config.setXmlToJsonNamespaces(getNamespaceMapping());
                MappedNamespaceConvention con = new MappedNamespaceConvention(config);
                XMLStreamWriter xmlStreamWriter = new MappedXMLStreamWriter(con, writer);
                Marshaller marshaller = createContext().createMarshaller();
                marshaller.marshal(element, xmlStreamWriter);
                outboundJSON =  writer.toString();
            }
            catch (JAXBException e)
            {
                throw new SuiteMarshallerException("Received JAXB exception while marshaling.", e);
            }
        }
        return outboundJSON;
    }

    @Override
    public Object unmarshal(String data) throws SuiteMarshallerException
    {
        Object result = null;
        log.warn("Unmarshalling json!  This is not yet working!");

        if (data != null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(data);
                Configuration config = new Configuration();
                config.setXmlToJsonNamespaces(getNamespaceMapping());
                config.setImplicitCollections(true);
                MappedNamespaceConvention con = new MappedNamespaceConvention(config);

                XMLStreamReader xmlStreamReader = new MappedXMLStreamReader(jsonObject, con);
                Unmarshaller unmarshaller = createContext().createUnmarshaller();
                result = unmarshaller.unmarshal(xmlStreamReader);
            }
            catch (Exception e)
            {
                throw new SuiteMarshallerException("Received exception while unmarshalling.", e);
            }
        }
        return result;
    }

    protected Map<String, String> getNamespaceMapping()
    {
        if(namespaceMappings == null)
        {
            namespaceMappings= new HashMap<String, String>();
            namespaceMappings.put("http://www.w3.org/2001/XMLSchema-instance", "schema");
            namespaceMappings.put("http://code.google.com/p/mash/schema/V1", "");
        }
        return namespaceMappings;
    }
}
