package org.mash.harness.message.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.JMSContext;
import javax.jms.JMSException;

/**
 *
 * @since Jan 29, 2010 3:59:56 PM
 *
 */
public class ConfigConnectionFactory implements ConnectionFactory
{
    private Connection connection;

    public ConfigConnectionFactory(Connection connection)
    {
        this.connection = connection;
    }

    public Connection createConnection() throws JMSException
    {
        return connection;
    }

    public Connection createConnection(String s, String s1) throws JMSException
    {
        return connection;
    }

    @Override
    public JMSContext createContext()
    {
        return null;
    }

    @Override
    public JMSContext createContext(String s, String s1)
    {
        return null;
    }

    @Override
    public JMSContext createContext(String s, String s1, int i)
    {
        return null;
    }

    @Override
    public JMSContext createContext(int i)
    {
        return null;
    }
}
