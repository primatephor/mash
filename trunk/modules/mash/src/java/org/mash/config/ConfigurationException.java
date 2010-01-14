package org.mash.config;

/**
 *
 * @author teastlack
 * @since Jul 9, 2009 2:43:32 PM
 *
 */
public class ConfigurationException extends Exception
{
    public ConfigurationException(String message)
    {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
