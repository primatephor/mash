package org.mash.harness;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 3:50:12 PM
 *
 */
public class OGNLResponse implements RunResponse
{
    private static final Logger log = Logger.getLogger(OGNLResponse.class.getName());
    private Object access;

    public OGNLResponse(Object access)
    {
        this.access = access;
    }

    public String getValue(String name)
    {
        String result = null;
        try
        {
            Object found = Ognl.getValue(name, access);
            if (found != null)
            {
                result = found.toString();
            }
        }
        catch (OgnlException e)
        {
            log.error("Unexpected error retrieving value " + name, e);
        }
        return result;
    }

    public Collection<String> getValues(String name)
    {
        Collection<String> result = new ArrayList<String>();
        try
        {
            Object found = Ognl.getValue(name, access);
            if (found != null && found instanceof Collection)
            {
                Collection foundResults = (Collection) found;
                for (Object foundResult : foundResults)
                {
                    result.add(foundResult.toString());
                }
            }
        }
        catch (OgnlException e)
        {
            log.error("Unexpected error retrieving value " + name, e);
        }
        return result;
    }

    public Collection<String> getValues()
    {
        throw new UnsupportedOperationException("Method getValues not implemented");
    }

    public String getString()
    {
        return access.toString();
    }

    @Override
    public String toString()
    {
        return getString();
    }
}
