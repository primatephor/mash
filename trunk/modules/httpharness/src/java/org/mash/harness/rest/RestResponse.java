package org.mash.harness.rest;

import com.meterware.httpunit.WebResponse;
import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;

import java.io.IOException;
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
public class RestResponse implements RunResponse
{
    private static final Logger LOG = Logger.getLogger(RestResponse.class.getName());
    private WebResponse webResponse;
    private XmlAccessor accessor;

    public RestResponse(WebResponse webResponse)
    {
        this.webResponse = webResponse;
        this.accessor = new XmlAccessor();
    }

    public String getValue(String expression)
    {
        String evaluation = null;
        String response = getString();

        if (response != null)
        {
            String[] result = accessor.getPath(response, expression);
            if (result != null && result.length > 0)
            {
                evaluation = result[0];
            }
        }

        return evaluation;
    }

    public Collection<String> getValues(String expression)
    {
        Collection<String> results = new ArrayList<String>();
        String response = getString();
        if (response != null)
        {
            String[] result = accessor.getPath(response, expression);
            if (result != null)
            {
                results.addAll(Arrays.asList(result));
            }
        }
        return results;
    }

    public Collection<String> getValues()
    {
        return Arrays.asList(getString());
    }

    public String getString()
    {
        String response = null;
        try
        {
            if (webResponse != null)
            {
                response = webResponse.getText();
            }
        }
        catch (IOException e)
        {
            LOG.error("Problem retrieving reponse from web request", e);
        }
        return response;
    }
}
