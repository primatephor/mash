package org.mash.harness.jms;

import org.apache.log4j.Logger;
import org.mash.harness.Message;
import org.mash.harness.SendException;

import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;

/**
 *
 * @author teastlack
 * @since Jan 22, 2010 9:51:58 AM
 *
 */
public class JMSEndpoint
{
    private static final Logger log = Logger.getLogger(JMSEndpoint.class.getName());

    private InitialContext initialContext;
    private ConnectionFactory connectionFactory;
    private Queue queue;
    private Connection connection = null;

    private long timeout = 5000l;
    private JMSMessageConverter converter;

    public JMSEndpoint(JMSMessageConverter converter)
    {
        this.converter = converter;
    }

    public JMSEndpoint(String providerUrl, String queueName) throws SendException
    {
        System.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url", providerUrl);
        try
        {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");
            log.debug("Creating connection for queue " + queueName);
            queue = (Queue) initialContext.lookup(queueName);
        }
        catch (NamingException e)
        {
            throw new SendException("Unexpected error looking up JMS context", e);
        }
    }

    public void send(Message message) throws SendException
    {
        Session session = null;
        try
        {
            log.debug("Creating JMS connection");
            session = establishSession();
            javax.jms.Message msg = converter.from(message, session);
            MessageProducer sender = session.createProducer(queue);
            log.debug("Sending message to JMS channel");
            sender.send(msg);
        }
        catch (JMSException e)
        {
            throw new SendException("Unexpected error sending JMS message", e);
        }
        finally
        {
            close(session);
            close(connection);
        }
    }

    public Message read() throws SendException
    {
        Session session = null;
        Message result = null;
        try
        {
            log.debug("Creating JMS connection");
            session = establishSession();
            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            javax.jms.Message jmsMessage = consumer.receive(timeout);
            result = converter.to(jmsMessage);
        }
        catch (JMSException e)
        {
            throw new SendException("Unexpected error sending JMS message", e);
        }
        finally
        {
            close(session);
            close(connection);
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

    private Session establishSession() throws SendException
    {
        Session result;
        try
        {
            if (connection != null)
            {
                close(connection);
            }
            connection = connectionFactory.createConnection();
            result = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        catch (JMSException e)
        {
            throw new SendException("Unexpected error sending JMS message", e);
        }

        return result;
    }

    public void addToEnvironment(String name, String value)
    {
        try
        {
            initialContext.addToEnvironment(name, value);
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

    public JMSMessageConverter getConverter()
    {
        return converter;
    }

    public void setConverter(JMSMessageConverter converter)
    {
        this.converter = converter;
    }
}
