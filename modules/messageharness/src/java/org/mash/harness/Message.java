package org.mash.harness;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author teastlack
 * @since Jan 22, 2010 9:47:49 AM
 *
 */
public class Message
{
    private String body;
    private Map<String,String> properties;

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
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
