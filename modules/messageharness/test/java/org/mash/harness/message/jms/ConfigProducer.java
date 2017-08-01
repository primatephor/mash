package org.mash.harness.message.jms;

import javax.jms.*;

/**
 *
 * @since Feb 1, 2010 9:37:45 AM
 *
 */
public class ConfigProducer implements MessageProducer
{
    Message message;

    public Message getMessage()
    {
        return message;
    }

    public void setDisableMessageID(boolean b) throws JMSException
    {
        throw new UnsupportedOperationException("Method setDisableMessageID not yet implemented");
    }

    public boolean getDisableMessageID() throws JMSException
    {
        throw new UnsupportedOperationException("Method getDisableMessageID not yet implemented");
    }

    public void setDisableMessageTimestamp(boolean b) throws JMSException
    {
        throw new UnsupportedOperationException("Method setDisableMessageTimestamp not yet implemented");
    }

    public boolean getDisableMessageTimestamp() throws JMSException
    {
        throw new UnsupportedOperationException("Method getDisableMessageTimestamp not yet implemented");
    }

    public void setDeliveryMode(int i) throws JMSException
    {
        throw new UnsupportedOperationException("Method setDeliveryMode not yet implemented");
    }

    public int getDeliveryMode() throws JMSException
    {
        throw new UnsupportedOperationException("Method getDeliveryMode not yet implemented");
    }

    public void setPriority(int i) throws JMSException
    {
        throw new UnsupportedOperationException("Method setPriority not yet implemented");
    }

    public int getPriority() throws JMSException
    {
        throw new UnsupportedOperationException("Method getPriority not yet implemented");
    }

    public void setTimeToLive(long l) throws JMSException
    {
        
    }

    public long getTimeToLive() throws JMSException
    {
        throw new UnsupportedOperationException("Method getTimeToLive not yet implemented");
    }

    public Destination getDestination() throws JMSException
    {
        throw new UnsupportedOperationException("Method getDestination not yet implemented");
    }

    public void close() throws JMSException
    {
        throw new UnsupportedOperationException("Method close not yet implemented");
    }

    public void send(Message message) throws JMSException
    {
        this.message = message;
    }

    public void send(Message message, int i, int i1, long l) throws JMSException
    {
        this.message = message;
    }

    public void send(Destination destination, Message message) throws JMSException
    {
        this.message = message;
    }

    public void send(Destination destination, Message message, int i, int i1, long l) throws JMSException
    {
        this.message = message;
    }

    @Override
    public void setDeliveryDelay(long l) throws JMSException
    {
        
    }

    @Override
    public long getDeliveryDelay() throws JMSException
    {
        return 0;
    }

    @Override
    public void send(Message message, CompletionListener completionListener) throws JMSException
    {

    }

    @Override
    public void send(Message message, int i, int i1, long l, CompletionListener completionListener) throws JMSException
    {

    }

    @Override
    public void send(Destination destination, Message message, CompletionListener completionListener) throws JMSException
    {

    }

    @Override
    public void send(Destination destination, Message message, int i, int i1, long l, CompletionListener completionListener) throws JMSException
    {

    }
}
