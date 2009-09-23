package org.mash.harness;

import org.apache.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.loader.HarnessConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Verify each parameter name/value pair exists within the given response.
 * <p/>
 * Setup information about containment is evaluated.  Multiple 'contains' configurations can be supplied, and the result
 * will be checked for each to ensure that it contains the value specified.  This is not a parameter as there may be a
 * parameter named 'contains' and as a configuration the harness can control that.
 *
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class StandardVerifyHarness extends BaseHarness implements VerifyHarness
{
    private static final Logger log = Logger.getLogger(StandardVerifyHarness.class.getName());
    private List<String> containment;

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        log.debug("Verifying parameters");
        RunResponse response = run.getResponse();
        for (Parameter parameter : parameters)
        {
            String responseValue = response.getValue(parameter.getName());
            if (parameter.getValue() != null)
            {
                if (!parameter.getValue().equals(responseValue))
                {
                    getErrors().add(new HarnessError(getName(), "Expected '" + parameter.getValue() +
                                                                "' does not equal '" + responseValue + "'"));
                }
            }
        }

        String responseText = response.getString();
        for (String s : getContainment())
        {
            if (!responseText.contains(s))
            {
                getErrors().add(new HarnessError(this.getName(),
                                                 "Not present in response:" + s));
            }
        }
    }

    @HarnessConfiguration(name = "contains")
    public void setContainment(String text)
    {
        log.debug("looking for '" + text + "' in text");
        getContainment().add(text);
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
