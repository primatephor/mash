package org.mash.harness.message.jms;

import org.mash.harness.message.Message;
import org.mash.harness.message.SendException;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * Transform a Message entity into a jms object message.
 * 
 * @author teastlack
 * @since Jan 26, 2010 5:23:19 PM
 *
 */
public class ObjectJMSAdapter extends JMSEndpointAdapter
{
    public ObjectJMSAdapter(String providerUrl, String queueName)
    {
        super(providerUrl, queueName);
    }

    public Message transform(javax.jms.Message jmsMessage) throws SendException
    {
        Message result = null;
        try
        {
            if (jmsMessage instanceof ObjectMessage)
            {
                ObjectMessage jmsMsg = (ObjectMessage) jmsMessage;
                result = new Message();
                result.setBody(jmsMsg.getObject());
            }
        }
        catch (JMSException e)
        {
            throw new SendException("Problem reading text message contents", e);
        }
        return result;
    }

    public javax.jms.Message transform(Message message) throws SendException
    {
        ObjectMessage jmsMsg;
        try
        {
            jmsMsg = getEndpoint().getConnectionData().getSession().createObjectMessage(message.getBody());
        }
        catch (JMSException e)
        {
            throw new SendException("Problem creating jms text message", e);
        }
        return jmsMsg;
    }
}
