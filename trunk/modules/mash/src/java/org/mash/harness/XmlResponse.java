package org.mash.harness;

import org.mash.tool.XmlAccessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * The rest response retrieves values from the response differently than the standard http response in that the name is
 * an xpath expression, so the response is expected to be an XML.
 *
 * @author: teastlack
 * @since: Jul 26, 2009
 */
public class XmlResponse implements RunResponse
{
    private String response;
    private XmlAccessor accessor;

    public XmlResponse(String response)
    {
        this.response = response;
    }

    public String getValue(String expression)
    {
        String evaluation = null;
        String[] result = getAccessor().getPath(expression);
        if (result != null && result.length > 0)
        {
            evaluation = result[0];
        }
        return evaluation;
    }

    public Collection<String> getValues(String expression)
    {
        Collection<String> results = new ArrayList<String>();
        String[] result = getAccessor().getPath(expression);
        if (result != null)
        {
            results.addAll(Arrays.asList(result));
        }
        return results;
    }

    public Collection<String> getValues()
    {
        return Arrays.asList(getString());
    }

    public String getString()
    {
        return response;
    }

    public XmlAccessor getAccessor()
    {
        if (accessor == null)
        {
            accessor = new XmlAccessor(getString());
        }
        return accessor;
    }

    @Override
    public String toString()
    {
        return getString();
    }
}
