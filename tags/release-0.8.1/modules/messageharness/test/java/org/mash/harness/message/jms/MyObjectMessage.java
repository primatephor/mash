package org.mash.harness.message.jms;

import java.io.Serializable;

/**
 *
 * @author teastlack
 * @since Feb 1, 2010 10:34:19 AM
 *
 */
public class MyObjectMessage implements Serializable
{
    private String name;
    private String value;

    public MyObjectMessage(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
