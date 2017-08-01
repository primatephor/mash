package org.mash.harness.message.jms;

import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.JMSException;
import javax.jms.ConnectionMetaData;
import javax.jms.ExceptionListener;
import javax.jms.ConnectionConsumer;
import javax.jms.Destination;
import javax.jms.ServerSessionPool;
import javax.jms.Topic;

/**
 *
 * @since Jan 29, 2010 4:00:29 PM
 *
 */
public class ConfigConnection implements Connection
{
    private Session session;

    public ConfigConnection(Session session)
    {
        this.session = session;
    }

    public Session createSession(boolean b, int i) throws JMSException
    {
        return session;
    }

    public String getClientID() throws JMSException
    {
        throw new UnsupportedOperationException("Method getClientID not yet implemented");
    }

    public void setClientID(String s) throws JMSException
    {
        throw new UnsupportedOperationException("Method setClientID not yet implemented");
    }

    public ConnectionMetaData getMetaData() throws JMSException
    {
        throw new UnsupportedOperationException("Method getMetaData not yet implemented");
    }

    public ExceptionListener getExceptionListener() throws JMSException
    {
        throw new UnsupportedOperationException("Method getExceptionListener not yet implemented");
    }

    public void setExceptionListener(ExceptionListener exceptionListener) throws JMSException
    {
        throw new UnsupportedOperationException("Method setExceptionListener not yet implemented");
    }

    public void start() throws JMSException
    {
    }

    public void stop() throws JMSException
    {
    }

    public void close() throws JMSException
    {
    }

    public ConnectionConsumer createConnectionConsumer(Destination destination, String s, ServerSessionPool serverSessionPool, int i) throws JMSException
    {
        throw new UnsupportedOperationException("Method createConnectionConsumer not yet implemented");
    }

    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String s, String s1, ServerSessionPool serverSessionPool, int i) throws JMSException
    {
        throw new UnsupportedOperationException("Method createDurableConnectionConsumer not yet implemented");
    }

    @Override
    public Session createSession(int i) throws JMSException
    {
        return null;
    }

    @Override
    public Session createSession() throws JMSException
    {
        return null;
    }

    @Override
    public ConnectionConsumer createSharedConnectionConsumer(Topic topic, String s, String s1, ServerSessionPool serverSessionPool, int i) throws JMSException
    {
        return null;
    }

    @Override
    public ConnectionConsumer createSharedDurableConnectionConsumer(Topic topic, String s, String s1, ServerSessionPool serverSessionPool, int i) throws JMSException
    {
        return null;
    }
}
