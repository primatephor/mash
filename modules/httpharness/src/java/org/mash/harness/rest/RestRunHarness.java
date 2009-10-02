package org.mash.harness.rest;

import org.apache.log4j.Logger;
import org.mash.config.Configuration;
import org.mash.config.Parameter;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.XmlResponse;
import org.mash.harness.http.HttpRunHarness;
import org.mash.harness.http.Method;
import org.mash.harness.http.StandardRequestFactory;

import java.io.IOException;
import java.util.List;

/**
 * Configurations:
 * <ul>
 * <li> 'clean' will create a new web conversation </li>
 * <li> 'url' is the resource url </li>
 * <li> 'verb' is the type of rest request ('CREATE', 'READ', 'UPDATE', 'DELETE').  REQUIRED</li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters are applied to the request type, and the request is invoked.  There are special parameters:
 * <ul>
 * <li> 'body' will be the streamed input.  This is required if not a 'read' or 'delete' as it's the resource in
 * question </li>
 * <li> 'content_type' is the type of body.  This isn't required, default is 'text/xml' </li>
 * </ul>
 *
 * @author: teastlack
 * @since: Jul 26, 2009
 */
public class RestRunHarness extends HttpRunHarness
{
    private static final Logger log = Logger.getLogger(RestRunHarness.class.getName());
    public static String DEFAULT_CONTENT_TYPE = "text/xml";

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        String type = getConfigurationValue("type");
        if (Method.POST.name().equalsIgnoreCase(type) ||
            Method.PUT.name().equalsIgnoreCase(type))
        {
            Parameter param = getParameter(StandardRequestFactory.BODY);
            if (param == null)
            {
                throw new IllegalArgumentException("'body' parameter is required for 'CREATE' or 'UPDATE' requests");
            }
        }
        super.run(previous, setups);
    }

    public RunResponse getResponse()
    {
        if (response == null)
        {
            String responseString = null;
            try
            {
                if (getWebResponse() != null)
                {
                    responseString = getWebResponse().getText();
                }
            }
            catch (IOException e)
            {
                log.error("Problem retrieving reponse from web request", e);
            }

            response = new XmlResponse(responseString);
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

    /**
     * Replace the CRUD verb specification with the appropriate Http type.
     *
     * @param configuration list of configs, including type
     */
    public void setConfiguration(List<Configuration> configuration)
    {
        boolean typePresent = false;
        for (Configuration config : configuration)
        {
            if ("verb".equals(config.getName()))
            {
                String type = config.getValue();
                Verb verb = Verb.valueOf(type);
                config.setValue(verb.getMethod().name());
                config.setName("type");
                typePresent = true;
            }
        }

        if (!typePresent)
        {
            throw new IllegalArgumentException("'verb' configuration MUST be present");
        }
        super.setConfiguration(configuration);
    }
}
