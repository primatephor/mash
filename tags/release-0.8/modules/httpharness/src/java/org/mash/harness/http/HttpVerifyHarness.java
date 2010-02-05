package org.mash.harness.http;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.StandardVerifyHarness;
import org.mash.loader.HarnessConfiguration;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Containment is overridden to allow for escaped characters.  That way we can verify things like '<', etc.
 *
 * Verify parameters returned from the web resource using the standard parameter matching.  Additional configurations
 * allow you to verify:
 * <ul>
 * <li> 'title' (title of the page)</li>
 * <li> 'status' (response code)</li>
 * </ul>
 *
 * Parameters names to search for could be:
 * <ul>
 * <li> html 'id' attribute name </li>
 * <li> html 'name' attribute name </li>
 * <li> xpath expression to parse html </li>
 * </ul>
 * So when names or ids of elements are present, this will use those.  If nothing is found, then an xpath
 * expression is run on the html using the parameter name.
 *
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class HttpVerifyHarness extends StandardVerifyHarness
{
    private static final Logger log = Logger.getLogger(HttpVerifyHarness.class);

    private String title;
    private String status;

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        log.debug("Verifying Http");
        if (run.getResponse() instanceof HttpResponse)
        {
            try
            {
                HttpResponse response = (HttpResponse) run.getResponse();
                if (title != null)
                {
                    HtmlPage pageResponse = (HtmlPage) response.getWebPage();
                    log.debug("Verifying title is " + title);
                    String expectedTitle = pageResponse.getTitleText();
                    if (!title.equals(expectedTitle))
                    {
                        getErrors().add(new HarnessError(this.getName(),
                                                         "Expected title '" + title +
                                                         "' doesn't equal actual '" + expectedTitle + "'"));
                    }
                }
                if (status != null)
                {
                    log.debug("Verifying status is " + status);
                    if (response.getWebPage() != null)
                    {
                        int expectedStatus = response.getWebPage().getWebResponse().getStatusCode();
                        if (Integer.valueOf(status) != expectedStatus)
                        {
                            getErrors().add(new HarnessError(this.getName(),
                                                             "Expected status '" + status +
                                                             "' doesn't equal actual '" + expectedStatus + "'"));
                        }
                    }
                    else
                    {
                        getErrors().add(new HarnessError(this.getName(), "Expected status '" + status +
                                                                         "' invalid, no response!"));
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
        else
        {
            log.warn("Not verifying response, not an HttpResponse");
        }

        if (!hasErrors())
        {
            super.verify(run, setup);
        }
    }

    @HarnessConfiguration(name = "contains")
    public void setContainment(String text)
    {
        text = StringEscapeUtils.unescapeHtml(text);
        log.debug("Unescaped:" + text);
        super.setContainment(text);
    }

    @HarnessConfiguration(name = "title")
    public void setTitle(String title)
    {
        this.title = title;
    }

    @HarnessConfiguration(name = "status")
    public void setStatus(String status)
    {
        this.status = status;
    }
}
