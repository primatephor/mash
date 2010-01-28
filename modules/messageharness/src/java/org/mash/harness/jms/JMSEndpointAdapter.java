package org.mash.harness.jms;

import org.mash.harness.Message;
import org.mash.harness.SendException;

import javax.jms.JMSException;
import java.util.Enumeration;

/**
 *
 * @author teastlack
 * @since Jan 26, 2010 5:12:03 PM
 *
 */
public abstract class JMSEndpointAdapter
{
    private JMSEndpoint endpoint;
    private String providerUrl;
    private String queueName;

    public JMSEndpointAdapter(String providerUrl, String queueName)
    {
        this.providerUrl = providerUrl;
        this.queueName = queueName;
    }

    /**
     * Transform the jms Message to the appropriate message type (text, object, xml, etc)
     *
     * @param jmsMessage to transform
     * @return message response
     * @throws SendException dur
     */
    public abstract Message transform(javax.jms.Message jmsMessage) throws SendException;

    /**
     * Transform the harness message to a jms message
     * @param message to transform
     * @return to send
     * @throws SendException dur
     */
    public abstract javax.jms.Message transform(Message message) throws SendException;

    public Message read() throws SendException
    {
        Message result = null;
        javax.jms.Message jmsMessage = getEndpoint().read();
        try
        {
            if (jmsMessage != null)
            {
                result = transform(jmsMessage);
                if (result != null)
                {
                    Enumeration props = jmsMessage.getPropertyNames();
                    while (props.hasMoreElements())
                    {
                        String name = String.valueOf(props.nextElement());
                        result.getProperties().put(name, jmsMessage.getStringProperty(name));
                    }
                }
            }
        }
        catch (JMSException e)
        {
            throw new SendException("Problem reading message from queue", e);
        }
        return result;
    }

    public void send(Message message) throws SendException
    {
        try
        {
            javax.jms.Message jmsMessage = transform(message);
            for (String key : message.getProperties().keySet())
            {
                jmsMessage.setStringProperty(key, message.getProperties().get(key));
            }
            getEndpoint().send(jmsMessage);
        }
        catch (JMSException e)
        {
            throw new SendException("Problem sending message", e);
        }
    }

    public JMSEndpoint getEndpoint() throws SendException
    {
        if (endpoint == null)
        {
            endpoint = new JMSEndpoint(providerUrl, queueName);
        }
        return endpoint;
    }

}
