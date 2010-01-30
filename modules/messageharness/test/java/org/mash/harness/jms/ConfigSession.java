package org.mash.harness.jms;

import javax.jms.Session;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.jms.QueueBrowser;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import java.io.Serializable;

/**
 *
 * @author teastlack
 * @since Jan 29, 2010 4:00:48 PM
 *
 */
public class ConfigSession implements Session
{
    public BytesMessage createBytesMessage() throws JMSException
    {
        throw new UnsupportedOperationException("Method createBytesMessage not yet implemented");
    }

    public MapMessage createMapMessage() throws JMSException
    {
        throw new UnsupportedOperationException("Method createMapMessage not yet implemented");
    }

    public Message createMessage() throws JMSException
    {
        throw new UnsupportedOperationException("Method createMessage not yet implemented");
    }

    public ObjectMessage createObjectMessage() throws JMSException
    {
        throw new UnsupportedOperationException("Method createObjectMessage not yet implemented");
    }

    public ObjectMessage createObjectMessage(Serializable serializable) throws JMSException
    {
        throw new UnsupportedOperationException("Method createObjectMessage not yet implemented");
    }

    public StreamMessage createStreamMessage() throws JMSException
    {
        throw new UnsupportedOperationException("Method createStreamMessage not yet implemented");
    }

    public TextMessage createTextMessage() throws JMSException
    {
        throw new UnsupportedOperationException("Method createTextMessage not yet implemented");
    }

    public TextMessage createTextMessage(String s) throws JMSException
    {
        throw new UnsupportedOperationException("Method createTextMessage not yet implemented");
    }

    public boolean getTransacted() throws JMSException
    {
        throw new UnsupportedOperationException("Method getTransacted not yet implemented");
    }

    public int getAcknowledgeMode() throws JMSException
    {
        throw new UnsupportedOperationException("Method getAcknowledgeMode not yet implemented");
    }

    public void commit() throws JMSException
    {
        throw new UnsupportedOperationException("Method commit not yet implemented");
    }

    public void rollback() throws JMSException
    {
        throw new UnsupportedOperationException("Method rollback not yet implemented");
    }

    public void close() throws JMSException
    {
        throw new UnsupportedOperationException("Method close not yet implemented");
    }

    public void recover() throws JMSException
    {
        throw new UnsupportedOperationException("Method recover not yet implemented");
    }

    public MessageListener getMessageListener() throws JMSException
    {
        throw new UnsupportedOperationException("Method getMessageListener not yet implemented");
    }

    public void setMessageListener(MessageListener messageListener) throws JMSException
    {
        throw new UnsupportedOperationException("Method setMessageListener not yet implemented");
    }

    public void run()
    {
        throw new UnsupportedOperationException("Method run not yet implemented");
    }

    public MessageProducer createProducer(Destination destination) throws JMSException
    {
        throw new UnsupportedOperationException("Method createProducer not yet implemented");
    }

    public MessageConsumer createConsumer(Destination destination) throws JMSException
    {
        throw new UnsupportedOperationException("Method createConsumer not yet implemented");
    }

    public MessageConsumer createConsumer(Destination destination, String s) throws JMSException
    {
        throw new UnsupportedOperationException("Method createConsumer not yet implemented");
    }

    public MessageConsumer createConsumer(Destination destination, String s, boolean b) throws JMSException
    {
        throw new UnsupportedOperationException("Method createConsumer not yet implemented");
    }

    public Queue createQueue(String s) throws JMSException
    {
        throw new UnsupportedOperationException("Method createQueue not yet implemented");
    }

    public Topic createTopic(String s) throws JMSException
    {
        throw new UnsupportedOperationException("Method createTopic not yet implemented");
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String s) throws JMSException
    {
        throw new UnsupportedOperationException("Method createDurableSubscriber not yet implemented");
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String s, String s1, boolean b) throws JMSException
    {
        throw new UnsupportedOperationException("Method createDurableSubscriber not yet implemented");
    }

    public QueueBrowser createBrowser(Queue queue) throws JMSException
    {
        throw new UnsupportedOperationException("Method createBrowser not yet implemented");
    }

    public QueueBrowser createBrowser(Queue queue, String s) throws JMSException
    {
        throw new UnsupportedOperationException("Method createBrowser not yet implemented");
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException
    {
        throw new UnsupportedOperationException("Method createTemporaryQueue not yet implemented");
    }

    public TemporaryTopic createTemporaryTopic() throws JMSException
    {
        throw new UnsupportedOperationException("Method createTemporaryTopic not yet implemented");
    }

    public void unsubscribe(String s) throws JMSException
    {
        throw new UnsupportedOperationException("Method unsubscribe not yet implemented");
    }
}
