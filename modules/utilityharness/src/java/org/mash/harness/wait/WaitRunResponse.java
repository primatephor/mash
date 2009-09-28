package org.mash.harness.wait;

import org.mash.harness.RunResponse;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author teastlack
 * @since Sep 28, 2009 12:17:46 PM
 *
 */
public class WaitRunResponse implements RunResponse
{
    private Long waitTime;

    public WaitRunResponse(Long waitTime)
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
}
