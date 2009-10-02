package org.mash.harness.ftp;

import org.mash.harness.RunResponse;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author teastlack
 * @since Sep 30, 2009 7:54:35 PM
 *
 */
public class RawResponse implements RunResponse
{
    private String[] results;

    public RawResponse(String[] results)
    {
        this.results = results;
    }

    public String getValue(String name)
    {
        String response = null;
        if (results.length > 0)
        {
            Integer index = new Integer(name);
            response = results[index];
        }
        return response;
    }

    public Collection<String> getValues(String name)
    {
        return getValues();
    }

    public Collection<String> getValues()
    {
        return Arrays.asList(results);
    }

    public String getString()
    {
        StringBuffer buffer = new StringBuffer();
        for (String result : results)
        {
            buffer.append(result).append("\n");
        }
        return buffer.toString();
    }
}
