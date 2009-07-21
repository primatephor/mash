package org.mash.harness.http;

import org.apache.log4j.Logger;
import org.mash.config.Configuration;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configurations: - 'clean' will create a new web conversation - 'url' is the url to submit to - 'type' is the type of
 * web request ('POST' or 'GET')
 * <p/>
 * Parameters are applied to the request type, and the request is invoked.
 *
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class HttpRunHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(HttpRunHarness.class.getName());
    private String url;
    private String type;
    private String clean;
    private HttpClient client;
    private HttpResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        log.debug("Running");
        if (clean != null && Boolean.valueOf(clean))
        {
            WebConversationHolder.reset();
        }

        if (client != null)
        {
            Map<String, String> params = new HashMap<String, String>();
            for (Parameter parameter : getParameters())
            {
                params.put(parameter.getName(), parameter.getValue());
            }
            client.submit(url, params);
        }
    }

    public RunResponse getResponse()
    {
        if (response == null)
        {
            response = new HttpResponse(client.getWebResponse());
        }
        return response;
    }

    public void setConfiguration(List<Configuration> configuration)
    {
        super.setConfiguration(configuration);
        for (Configuration config : configuration)
        {
            if ("url".equals(config.getName()))
            {
                url = config.getValue();
            }
            if ("type".equals(config.getName()))
            {
                type = config.getValue();
            }
            if ("clean".equals(config.getName()))
            {
                clean = config.getValue();
            }
        }
        client = Method.valueOf(type).getClient();
    }
}
