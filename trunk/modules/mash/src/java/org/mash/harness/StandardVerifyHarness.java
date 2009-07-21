package org.mash.harness;

import org.apache.log4j.Logger;
import org.mash.config.Parameter;

import java.util.List;

/**
 * Verify each parameter name/value pair exists within the given response.
 * <p/>
 * Setup information is ignored with the standard harness.
 *
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class StandardVerifyHarness extends BaseHarness implements VerifyHarness
{
    private static final Logger log = Logger.getLogger(StandardVerifyHarness.class.getName());

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        log.debug("Verifying");
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
    }
}
