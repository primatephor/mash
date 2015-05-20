package org.mash.harness.message.jms;

import org.mash.harness.message.SendException;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.JMSException;

/**
 *
 * @author
 * @since Jan 29, 2010 3:31:55 PM
 *
 */
public class ConnectionData
{
    private static final Logger log = Logger.getLogger(ConnectionData.class.getName());

    private Context initialContext;
    private ConnectionFactory connectionFactory;
    private Queue queue;
    private Connection connection = null;
    private Session session;

    public ConnectionData(String providerUrl, String queueName) throws SendException
    {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        System.setProperty(Context.PROVIDER_URL, providerUrl);
        System.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
        try
        {
            initialContext = new InitialContext();
            init(queueName);
        }
        catch (NamingException e)
        {
            throw new SendException("Unexpected error looking up JMS context", e);
        }
    }

    //for testing
    public ConnectionData(Context context, String queueName) throws SendException
    {
        this.initialContext = context;
        try
        {
            init(queueName);
        }
        catch (NamingException e)
        {
            throw new SendException("Unexpected error looking up JMS context", e);
        }
    }

    private void init(String queueName) throws NamingException
    {
        connectionFactory = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");
        log.debug("Creating connection for queue " + queueName);
        queue = (Queue) initialContext.lookup(queueName);
    }

    public Session getSession() throws SendException
    {
        if (session == null ||
            connection == null)
        {
            try
            {
                connection = connectionFactory.createConnection();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            }
            catch (JMSException e)
            {
                throw new SendException("Unexpected error sending JMS message", e);
            }
        }
        return session;
    }

    public Connection getConnection()
    {
        return connection;
    }

    public Queue getQueue()
    {
        return queue;
    }

    public void addToEnvironment(String name, String value) throws NamingException
    {
        initialContext.addToEnvironment(name, value);
    }
}
