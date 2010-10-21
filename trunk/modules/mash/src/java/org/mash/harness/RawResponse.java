package org.mash.harness;

import org.mash.tool.StringUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author teastlack
 * @since Sep 30, 2009 7:54:35 PM
 *
 */
public class RawResponse implements RunResponse
{
    private String[] results;

    public RawResponse(String... results)
    {
        this.results = results;
    }

    public String getValue(String name)
    {
        String response = null;
        if (results != null &&
            results.length > 0)
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
        if (results != null)
        {
            return Arrays.asList(results);
        }
        return Collections.emptyList();
    }

    public String getString()
    {
        String result = null;
        if (results != null)
        {
            result = StringUtil.toString(results);
        }
        return result;
    }

    @Override
    public String toString()
    {
        return getString();
    }
}

