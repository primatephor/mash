package org.mash.loader;

/**
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public class HarnessException extends Exception
{
    public HarnessException(String s)
    {
        super(s);
    }

    public HarnessException(String s, Throwable throwable)
    {
        super(s, throwable);
    }
}
