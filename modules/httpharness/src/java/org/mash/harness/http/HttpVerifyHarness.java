package org.mash.harness.http;

import org.apache.log4j.Logger;
import org.mash.config.Configuration;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.StandardVerifyHarness;

import java.util.ArrayList;
import java.util.List;

/**
 * Verify parameters returned from the web resource using the standard parameter matching.  Additional configurations
 * allow you to verify: <p/>- title (title of the page) <p/>- status (response code) <p/>- contains (any string must be
 * contained by the page)
 *
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class HttpVerifyHarness extends StandardVerifyHarness
{
    private static final Logger log = Logger.getLogger(HttpResponse.class);

    private String title;
    private String status;
    private List<String> containment;

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        log.debug("Verifying");
        if (run.getResponse() instanceof HttpResponse)
        {
            try
            {
                HttpResponse response = (HttpResponse) run.getResponse();
                if (title != null)
                {
                    String expectedTitle = response.getWebResponse().getTitle();
                    if (!title.equals(expectedTitle))
                    {
                        getErrors().add(new HarnessError(this.getName(),
                                                         "Expected title '" + title +
                                                         "' doesn't equal actual '" + expectedTitle + "'"));
                    }
                }
                if (status != null)
                {
                    int expectedStatus = response.getWebResponse().getResponseCode();
                    if (Integer.valueOf(status) != expectedStatus)
                    {
                        getErrors().add(new HarnessError(this.getName(),
                                                         "Expected status '" + status +
                                                         "' doesn't equal actual '" + expectedStatus + "'"));
                    }
                }
            }
            catch (Exception e)
            {
                log.error("Problem retrieving data from web response", e);
                getErrors().add(new HarnessError(this.getName(),
                                                 "Problem retrieving data from web response:" + e.getMessage()));
            }
        }

        String response = run.getResponse().getString();
        for (String s : getContainment())
        {
            if (!response.contains(s))
            {
                getErrors().add(new HarnessError(this.getName(),
                                                 "Not present in response:" + s));
            }
        }
        super.verify(run, setup);
    }

    public void setConfiguration(List<Configuration> configuration)
    {
        super.setConfiguration(configuration);
        for (Configuration config : configuration)
        {
            if ("title".equals(config.getName()))
            {
                title = config.getValue();
            }
            if ("status".equals(config.getName()))
            {
                status = config.getValue();
            }
            if ("contains".equals(config.getName()))
            {
                getContainment().add(config.getValue());
            }
        }
    }

    public List<String> getContainment()
    {
        if (containment == null)
        {
            containment = new ArrayList<String>();
        }
        return containment;
    }
}