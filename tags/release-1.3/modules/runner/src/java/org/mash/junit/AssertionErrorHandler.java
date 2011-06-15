package org.mash.junit;

import junit.framework.AssertionFailedError;
import org.mash.config.ScriptDefinition;
import org.mash.harness.HarnessError;
import org.mash.tool.ErrorFormatter;
import org.mash.tool.ErrorHandler;

import java.util.List;

/**
 * If errors are present, must throw errors
 *
 * @author teastlack
 * @since Sep 8, 2010 10:14:23 AM
 */
public class AssertionErrorHandler extends ErrorHandler
{
    public AssertionErrorHandler(ErrorFormatter formatter)
    {
        super(formatter);
    }

    @Override
    public void handleErrors(List<HarnessError> errors, ScriptDefinition script)
    {
        super.handleErrors(errors, script);
        if(isError())
        {
            //just the first one should suffice for junit assertions
            throw new AssertionFailedError(getFormattedErrors().get(0));
        }
    }
}
