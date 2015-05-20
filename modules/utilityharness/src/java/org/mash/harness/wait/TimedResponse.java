package org.mash.harness.wait;

import org.mash.harness.RunResponse;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author
 * @since Sep 28, 2009 12:17:46 PM
 */
public class TimedResponse implements RunResponse
{
    private Long waitTime;

    public TimedResponse(Long waitTime)
    {
        this.waitTime = waitTime;
    }

    public String getValue(String name)
    {
        return getString();
    }

    public Collection<String> getValues(String name)
    {
        return getValues();
    }

    public Collection<String> getValues()
    {
        return Arrays.asList(getString());
    }

    public String getString()
    {
        return waitTime.toString();
    }

    @Override
    public String toString()
    {
        return "Wait Time:" + getString();
    }
}
