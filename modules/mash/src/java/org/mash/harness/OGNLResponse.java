package org.mash.harness;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.tool.DefaultMemberAccess;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author
 * @since Sep 22, 2009 3:50:12 PM
 *
 */
public class OGNLResponse implements RunResponse
{
    private static final Logger log = LogManager.getLogger(OGNLResponse.class.getName());
    private Object access;
    private OgnlContext context;

    public OGNLResponse(Object access)
    {
        this.access = access;
        context = new OgnlContext(null, null, new DefaultMemberAccess(true));
    }

    public String getValue(String name)
    {
        String result = null;
        try
        {
            Object found = Ognl.getValue(name, context, access);
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
            Object found = Ognl.getValue(name, context, access);
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
