package org.mash.harness.message.jms;

import org.apache.log4j.Logger;
import org.mash.harness.message.SendException;

import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Message;

/**
 *
 * @author
 * @since Jan 22, 2010 9:51:58 AM
 *
 */
public class JMSEndpoint
{
    private static final Logger log = Logger.getLogger(JMSEndpoint.class.getName());
    private ConnectionData connectionData;
    private long timeout = 10000l;

    public JMSEndpoint(String providerUrl, String queueName) throws SendException
    {
        connectionData = new ConnectionData(providerUrl, queueName);
    }

    public JMSEndpoint(ConnectionData connectionData)
    {
        this.connectionData = connectionData;
    }

    public void send(Message message) throws SendException
    {
        Session session = null;
        try
        {
            log.debug("Creating JMS connection");
            session = connectionData.getSession();
            MessageProducer sender = session.createProducer(connectionData.getQueue());
            sender.setTimeToLive(timeout);
            log.debug("Sending message to JMS channel");
            sender.send(message);
        }
        catch (JMSException e)
        {
            throw new SendException("Unexpected error sending JMS message", e);
        }
        finally
        {
            close(session);
            close(connectionData.getConnection());
        }
    }

    public Message read() throws SendException
    {
        Session session = null;
        Message result = null;
        try
        {
            log.debug("Creating JMS connection");
            session = connectionData.getSession();
            MessageConsumer consumer = session.createConsumer(connectionData.getQueue());
            log.debug("Reading from queue");
            connectionData.getConnection().start();
            result = consumer.receive(timeout);
        }
        catch (JMSException e)
        {
            throw new SendException("Unexpected error sending JMS message", e);
        }
        finally
        {
            close(session);
            close(connectionData.getConnection());
        }
        return result;
    }

    private void close(Session session)
    {
        if (session != null)
        {
            try
            {
                log.debug("Closing JMS session");
                session.close();
            }
            catch (JMSException e)
            {
                log.error("Unexpected error closing session", e);
            }
        }
    }

    private void close(Connection connection)
    {
        if (connection != null)
        {
            try
            {
                log.debug("Closing JMS connection");
                connection.close();
            }
            catch (JMSException e)
            {
                log.error("Unexpected error closing connection", e);
            }
        }
    }

    public void addToEnvironment(String name, String value)
    {
        try
        {
            connectionData.addToEnvironment(name, value);
        }
        catch (Exception e)
        {
            log.error("Unexpected error creating jms connection data", e);
        }
    }

    public long getTimeout()
    {
        return timeout;
    }

    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
    }

    public ConnectionData getConnectionData()
    {
        return connectionData;
    }
}
