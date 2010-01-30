package org.mash.harness.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.JMSException;

/**
 *
 * @author teastlack
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
}
