package org.mash.tool;

import org.apache.log4j.Logger;
import org.mash.harness.HarnessError;

import java.util.ArrayList;
import java.util.List;

/**
 * Objects that run the scripts will deal with errors differently.  This base class allows for logging, and other
 * entities would extend or do other work.
 *
 * @author teastlack
 * @since Sep 8, 2010 10:03:55 AM
 */
public class ErrorHandler
{
    private static final Logger log = Logger.getLogger(ErrorHandler.class.getName());

    private ErrorFormatter formatter;
    private List<String> formattedErrors;
    private List<HarnessError> harnessErrors;
    private boolean error = false;

    public ErrorHandler(ErrorFormatter formatter)
    {
        this.formatter = formatter;
    }

    public void handleErrors(List<HarnessError> errors)
    {
        if (errors.size() > 0)
        {
            getHarnessErrors().addAll(errors);
            this.error = true;
            getFormattedErrors().add(formatter.format(errors));
            log.error(formattedErrors);
        }
    }

    public List<HarnessError> getHarnessErrors()
    {
        if(harnessErrors == null)
        {
            harnessErrors = new ArrayList<HarnessError>();
        }
        return harnessErrors;
    }

    public List<String> getFormattedErrors()
    {
        if(formattedErrors == null)
        {
            formattedErrors = new ArrayList<String>();
        }
        return formattedErrors;
    }

    public boolean isError()
    {
        return error;
    }
}