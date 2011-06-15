package org.mash.harness.message;

/**
 *
 * @author teastlack
 * @since Jan 22, 2010 1:43:21 PM
 *
 */
public class SendException extends Exception
{
    public SendException()
    {
    }

    public SendException(String message)
    {
        super(message);
    }

    public SendException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SendException(Throwable cause)
    {
        super(cause);
    }
}
