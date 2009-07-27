package org.mash.loader;

/**
 * The marshaller needs to take some string data and create suite objects in the xml package.  This interface exists
 * because there may be other non xml data strings that testers may want to work with.
 * <p/>
 * A System test need only retrieve the marshaller in question and read that file data to create the appropriate suite
 * objects for the framework.
 * <p/>
 * User: teastlack Date: Jul 1, 2009 Time: 11:21:00 AM
 */
public interface SuiteMarshaller
{
    /**
     * Marshal the submitted object to an XML string
     *
     * @param element defined in the xml package
     * @return XML as string
     * @throws SuiteMarshallerException when marshalling is in error
     */
    public String marshal(Object element) throws SuiteMarshallerException;

    /**
     * Unmarshal the data and create the appropriate objects defined in the xml package
     *
     * @param data as a string
     * @return suite object
     * @throws SuiteMarshallerException when unmarshalling causes an error
     */
    public Object unmarshal(String data) throws SuiteMarshallerException;
}
