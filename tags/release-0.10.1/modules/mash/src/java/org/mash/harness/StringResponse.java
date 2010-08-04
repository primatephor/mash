package org.mash.harness;

import java.util.Collection;

/**
 * This only supports 'contains' verification operations.  The assumption is that this is a text blob.
 *
 * @author teastlack
 * @since Jan 29, 2010 9:09:10 AM
 *
 */
public class StringResponse implements RunResponse
{
    private String text;

    public StringResponse(String text)
    {
        this.text = text;
    }

    public String getValue(String name)
    {
        throw new UnsupportedOperationException("Method getValue not supported");
    }

    public Collection<String> getValues(String name)
    {
        throw new UnsupportedOperationException("Method getValues not supported");
    }

    public Collection<String> getValues()
    {
        throw new UnsupportedOperationException("Method getValues not supported");
    }

    public String getString()
    {
        return text;
    }
}
