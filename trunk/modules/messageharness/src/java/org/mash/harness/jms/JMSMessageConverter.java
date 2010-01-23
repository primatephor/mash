package org.mash.harness.jms;

import org.mash.harness.Message;

import javax.jms.Session;
import javax.jms.JMSException;

/**
 * Message converters are used to separate the message types from the actual jms endpoint.  Makes
 * life a little easier when adding new message types.
 * 
 * @author teastlack
 * @since Jan 22, 2010 2:46:46 PM
 *
 */
public interface JMSMessageConverter
{
    /**
     * Convert to a mash Message
     *
     * @param jmsMessage to convert from
     * @return mash message
     * @throws javax.jms.JMSException when problem working with jms
     */
    Message to(javax.jms.Message jmsMessage) throws JMSException;

    /**
     * Convert from a mash message
     * @param message to convert from
     * @param session to create new message from
     * @return jms message
     * @throws javax.jms.JMSException when problem working with jms
     */
    javax.jms.Message from(Message message, Session session) throws JMSException;
}
