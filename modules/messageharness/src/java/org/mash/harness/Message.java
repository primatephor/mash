package org.mash.harness;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

/**
 *
 * @author teastlack
 * @since Jan 22, 2010 9:47:49 AM
 *
 */
public class Message
{
    private Serializable body;
    private Map<String,String> properties;

    public Serializable getBody()
    {
        return body;
    }

    public void setBody(Serializable body)
    {
        this.body = body;
    }

    public Map<String, String> getProperties()
    {
        if(properties == null)
        {
            properties = new HashMap<String, String>();
        }
        return properties;
    }
}
