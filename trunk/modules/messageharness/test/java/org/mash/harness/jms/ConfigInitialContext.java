package org.mash.harness.jms;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author teastlack
 * @since Jan 29, 2010 3:45:56 PM
 *
 */
public class ConfigInitialContext extends InitialContext
{
    private Map<String, Object> data = new HashMap<String, Object>();

    public ConfigInitialContext() throws NamingException
    {
    }

    public Object lookup(String name) throws NamingException
    {
        return data.get(name);
    }

    public void addData(String name, Object value)
    {
        data.put(name, value);
    }
}
