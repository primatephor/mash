package org.mash.harness;

import org.apache.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.loader.HarnessConfiguration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Verify dates for response.  This is broken out because so many dates have different formats, etc.
 * <p/>
 * <p/>
 * Configurations:
 * <ul>
 * <li> 'format' is the format of the response AND date parameters (common xml: yyyy-MM-dd'T'HH:mm:ss)</li>
 * <li> 'range_millis' is the range in milliseconds that the response date must fall within (default is 0)</li>
 * </ul>
 * <p/>
 * Parameters:
 * Parameters are pulled from the response like the StandardVerifyHarness, and the dates are converted to milliseconds
 * for comparison.
 *
 * @author teastlack
 * @since Sep 27, 2010 10:34:46 AM
 */
public class DateVerifyHarness extends BaseHarness implements VerifyHarness
{
    private static final Logger log = Logger.getLogger(DateVerifyHarness.class.getName());

    private String format;
    private long rangeMillis = 0;
    private DateFormat formatter;

    public void verify(RunHarness run,
                       List<SetupHarness> setup)
    {
        if (this.format != null)
        {
            formatter = new SimpleDateFormat(this.format);
            verifyParameters(run.getResponse());
        }
        else
        {
            addError("Format Required", "Date format is necessary to parse expected date in response");
        }
    }

    protected void verifyParameters(RunResponse response)
    {
        if (parameters != null)
        {
            for (Parameter parameter : parameters)
            {
                log.debug("Checking that '" + parameter.getName() + "' equals '" + parameter.getValue() + "'");
                String responseValue = response.getValue(parameter.getName());
                if (parameter.getValue() != null)
                {
                    String checkValue = parameter.getValue();
                    Date responseDate = null;
                    Date checkDate = null;
                    try
                    {
                        responseDate = formatter.parse(responseValue);
                    }
                    catch (ParseException e)
                    {
                        addError("Problem parsing response value " + responseValue, e);
                    }
                    try
                    {
                        checkDate = formatter.parse(checkValue);
                    }
                    catch (ParseException e)
                    {
                        addError("Problem parsing verify value " + responseValue, e);
                    }

                    if (responseDate != null && checkDate != null)
                    {
                        long min = checkDate.getTime() - rangeMillis;
                        long max = checkDate.getTime() + rangeMillis;
                        long responseMillis = responseDate.getTime();

                        if (responseMillis < min || responseMillis > max)
                        {
                            addError(parameter.getName()+" is out of range", "Response date " + responseDate +
                                    ", ms:" + responseDate.getTime() +
                                    " is out of range of expected date " + checkDate +
                                    ", ms:" + checkDate.getTime());
                        }
                    }
                }
                else
                {
                    if (responseValue != null)
                    {
                        addError("Expected Response Not Null", "Expected NULL date for " + parameter.getName() +
                                ", got " + responseValue);
                    }
                }
            }
        }
        else
        {
            addError("Date Parameter", "At least one date parameter for validation is required");
        }
    }

    @HarnessConfiguration(name = "format")
    public void setFormat(String format)
    {
        this.format = format;
    }

    @HarnessConfiguration(name = "range_millis")
    public void setRangeMillis(String rangeMillis)
    {
        this.rangeMillis = Long.valueOf(rangeMillis);
    }
}
