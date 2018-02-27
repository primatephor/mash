package org.mash.loader.accessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.config.BaseParameter;
import org.mash.config.Response;
import org.mash.harness.RunHarness;
import org.mash.loader.ContentAccessor;

import java.util.List;

/**
 * Access the previous runs (ignoring current content) and return the response found.  This finds the run named in the
 * Response configuration name attribute.  Once that run is found, the response is queried for the name/value pair
 * contained in the Repsonse configuration value.
 * <p/>
 * For example, if there is a run with the name 'login' that contains the value 'session' that we want to use, the
 * Response configuration would look like <Response name='login'>session</Response>
 *
 * @author
 * @since Jul 10, 2009 1:04:56 PM
 */
public class ResponseAccessor implements ContentAccessor
{
    private static final Logger log = LogManager.getLogger(ResponseAccessor.class.getName());
    private List<RunHarness> previousRun;

    public ResponseAccessor(List<RunHarness> previousRun)
    {
        this.previousRun = previousRun;
    }

    public String accessContent(BaseParameter parameter, String currentContent) throws Exception
    {
        String result = "";
        if (currentContent != null)
        {
            result = currentContent;
        }
        if (parameter.getResponse() != null)
        {
            Response response = parameter.getResponse();
            RunHarness run = getRun(response.getName(), previousRun);
            if (run != null)
            {
                if (log.isTraceEnabled())
                {
                    log.trace("Searching for '" + response.getValue() + "' in " + run.getDefinition().getName());
                }
                if (run.getResponse() != null)
                {
                    result = run.getResponse().getValue(response.getValue());
                    if (null != result && Boolean.TRUE.equals(response.getTrim()))
                    {
                        result = result.trim();
                    }
                    log.trace("Found result '" + result + "' in " + run.getDefinition().getName());
                }
                else
                {
                    log.error("Response from run '" + response.getName() + "' is null!");
                }
            }
            else
            {
                log.warn("Could not find a run with name '" + response.getName() + "'");
            }
        }
        return result;
    }

    private RunHarness getRun(String runName, List<RunHarness> previousRun)
    {
        RunHarness result = null;
        //start looking from the end, as we want the most recent
        for (int i = previousRun.size()-1; i >= 0; i--)
        {
            RunHarness runHarness = previousRun.get(i);
            if (runName != null && runName.equals(runHarness.getDefinition().getName()))
            {
                result = runHarness;
                break;
            }
        }
        return result;
    }
}
