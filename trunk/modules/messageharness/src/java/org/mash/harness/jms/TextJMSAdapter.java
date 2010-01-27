package org.mash.harness.jms;

import org.mash.harness.Message;
import org.mash.harness.SendException;

import javax.jms.TextMessage;
import javax.jms.JMSException;

/**
 * This converts a jms text message.  Other converters will come up over time.
 *
 * @author teastlack
 * @since Jan 22, 2010 2:46:17 PM
 *
 */
public class TextJMSAdapter extends JMSEndpointAdapter
{

    public TextJMSAdapter(String providerUrl, String queueName)
    {
        super(providerUrl, queueName);
    }

    public Message transform(javax.jms.Message jmsMessage) throws SendException
    {
        Message result = null;
        try
        {
            if (jmsMessage instanceof TextMessage)
            {
                TextMessage txMsg = (TextMessage) jmsMessage;
                result = new Message();
                result.setBody(txMsg.getText());
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
        TextMessage textMessage;
        try
        {
            textMessage = getEndpoint().getSession().createTextMessage(String.valueOf(message.getBody()));
        }
        catch (JMSException e)
        {
            throw new SendException("Problem creating jms text message", e);
        }
        return textMessage;
    }
}
