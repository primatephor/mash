package org.mash.loader;

import org.mash.config.BaseParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this chain to manage the order that the accessors are run.  Accessors are added to the chain in order of priority
 * and each one is processed with the previous accessor result.  This allows some accessors to just retrieve data and
 * rely on another to process that data (like replace text strings). 
 *
 * @author
 * @since Jul 10, 2009 10:50:36 AM
 *
 */
public class AccessorChain
{
    private ArrayList<ContentAccessor> accessors;

    private List<ContentAccessor> getAccessors()
    {
        if (accessors == null)
        {
            accessors = new ArrayList<ContentAccessor>();
        }
        return accessors;
    }

    public void add(ContentAccessor accessor)
    {
        getAccessors().add(accessor);
    }

    public String access(BaseParameter parameter) throws Exception
    {
        String result = null;
        for (ContentAccessor contentAccessor : getAccessors())
        {
            result = contentAccessor.accessContent(parameter, result);
        }
        return result;
    }
}
