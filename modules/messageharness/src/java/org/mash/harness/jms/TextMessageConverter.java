package org.mash.harness.jms;

import org.mash.harness.Message;

import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import java.util.Enumeration;

/**
 * This converts a jms text message.  Other converters will come up over time.
 * 
 * @author teastlack
 * @since Jan 22, 2010 2:46:17 PM
 *
 */
public class TextMessageConverter implements JMSMessageConverter
{
    public Message to(javax.jms.Message jmsMessage) throws JMSException
    {
        Message result = null;
        if (jmsMessage != null && jmsMessage instanceof TextMessage)
        {
            TextMessage txMsg = (TextMessage) jmsMessage;
            result = new Message();
            result.setBody(txMsg.getText());
            Enumeration props = jmsMessage.getPropertyNames();
            while (props.hasMoreElements())
            {
                String name = String.valueOf(props.nextElement());
                result.getProperties().put(name, jmsMessage.getStringProperty(name));
            }
        }
        return result;
    }

    public javax.jms.Message from(Message message, Session session) throws JMSException
    {
        TextMessage textMessage = session.createTextMessage(message.getBody());
        for (String key : message.getProperties().keySet())
        {
            textMessage.setStringProperty(key, message.getProperties().get(key));
        }
        return textMessage;
    }
}
