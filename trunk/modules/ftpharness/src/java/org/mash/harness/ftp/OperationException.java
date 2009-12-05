package org.mash.harness.ftp;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 2:46:30 PM
 *
 */
public class OperationException extends Exception
{
    public OperationException(String message)
    {
        super(message);
    }

    public OperationException(String s, Exception e)
    {
        super(s, e);
    }
}
