package org.mash.harness.rest;

import org.mash.harness.http.HttpRunHarness;
import org.mash.harness.http.StandardRequestFactory;
import org.mash.harness.RunResponse;
import org.mash.config.Parameter;

import java.util.List;

/**
 * @author: teastlack
 * @since: Jul 26, 2009
 */
public class RestRunHarness extends HttpRunHarness
{
    public static String DEFAULT_CONTENT_TYPE = "text/xml";

    public RunResponse getResponse()
    {
        if (response == null)
        {
            response = new RestResponse(getWebResponse());
        }
        return response;
    }

    public void setParameters(List<Parameter> parameters)
    {
        Boolean contentTypePresent = false;
        for (Parameter parameter : parameters)
        {
            if (StandardRequestFactory.CONTENT_TYPE.equals(parameter.getName()))
            {
                contentTypePresent = true;
                break;
            }
        }

        if (!contentTypePresent)
        {
            parameters.add(new Parameter(StandardRequestFactory.CONTENT_TYPE, "text/xml"));
        }
        super.setParameters(parameters);
    }
}
