package org.mash.harness.jms;

import javax.jms.MessageConsumer;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Message;

/**
 *
 * @author teastlack
 * @since Feb 1, 2010 10:20:54 AM
 *
 */
public class ConfigConsumer implements MessageConsumer
{
    private Message msg;

    public ConfigConsumer(Message msg)
    {
        this.msg = msg;
    }

    public String getMessageSelector() throws JMSException
    {
        throw new UnsupportedOperationException("Method getMessageSelector not yet implemented");
    }

    public MessageListener getMessageListener() throws JMSException
    {
        throw new UnsupportedOperationException("Method getMessageListener not yet implemented");
    }

    public void setMessageListener(MessageListener messageListener) throws JMSException
    {
        throw new UnsupportedOperationException("Method setMessageListener not yet implemented");
    }

    public Message receive() throws JMSException
    {
        return msg;
    }

    public Message receive(long l) throws JMSException
    {
        return msg;
    }

    public Message receiveNoWait() throws JMSException
    {
        return msg;
    }

    public void close() throws JMSException
    {
    }
}
