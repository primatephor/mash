package org.mash.harness.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.RunResponse;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * provides access to message properties for verification
 * 
 * @author
 * @since Jan 29, 2010 3:19:20 PM
 *
 */
public class MessagePropertyResponse implements RunResponse
{
    private static final Logger log = LogManager.getLogger(MessagePropertyResponse.class.getName());
    private RunResponse target;
    private Map<String, String> properties;

    public MessagePropertyResponse(RunResponse target,
                                   Map<String, String> properties)
    {
        this.target = target;
        this.properties = properties;
    }

    public Map<String, String> getProperties()
    {
        if (properties == null)
        {
            properties = new HashMap<String, String>();
        }
        return properties;
    }

    public String getValue(String name)
    {
        String result = getProperties().get(name);
        if (result == null)
        {
            try
            {
                result = target.getValue(name);
            }
            catch (Exception e)
            {
                log.error("Unexpected error calling target getValue", e);
            }
        }
        return result;
    }

    public Collection<String> getValues(String name)
    {
        return target.getValues(name);
    }

    public Collection<String> getValues()
    {
        return target.getValues();
    }

    public String getString()
    {
        return target.getString();
    }
}
