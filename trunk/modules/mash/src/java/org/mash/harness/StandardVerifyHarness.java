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
    private Boolean ignoreSpaces = false;

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        log.debug("Verifying parameters");
        RunResponse response = run.getResponse();
        verifyParameters(response);

        String responseText = response.getString();
        if (responseText != null)
        {
            for (String s : getContainment())
            {
                if (!responseText.contains(s))
                {
                    getErrors().add(new HarnessError(this.getName(), "Not present in response:" + s));
                }
            }
        }
        else
        {
            getErrors().add(new HarnessError(this.getName(), "No response!"));
        }
    }

    protected void verifyParameters(RunResponse response)
    {
        for (Parameter parameter : parameters)
        {
            String responseValue = response.getValue(parameter.getName());
            if (parameter.getValue() != null)
            {
                String checkValue = parameter.getValue();
                if (ignoreSpaces && responseValue != null)
                {
                    responseValue = responseValue.replaceAll("\\s+", "");
                    checkValue = checkValue.replaceAll("\\s+", "");
                }

                if (checkValue != null && checkValue.length() > 0)
                {
                    //if is not null or blank, then response must be equal
                    if (!checkValue.equals(responseValue))
                    {
                        getErrors().add(new HarnessError(getName(), "Expected " + parameter.getName() + " '" +
                                                                    parameter.getValue() +
                                                                    "' does not equal '" + responseValue + "'"));
                    }
                }
                else
                {
                    //if check is null or blank, then response must be null or blank
                    if (responseValue != null && responseValue.length() > 0)
                    {
                        getErrors().add(new HarnessError(getName(), "Expected " + parameter.getName() +
                                                                    " to be empty, was '" + responseValue + "'"));
                    }
                }
            }
        }
    }

    @HarnessConfiguration(name = "contains")
    public void setContainment(String text)
    {
        log.debug("looking for '" + text + "' in text");
        getContainment().add(text);
    }

    @HarnessConfiguration(name = "ignore_space")
    public void setIgnoreSpaces(String ignore)
    {
        this.ignoreSpaces = Boolean.valueOf(ignore);
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
