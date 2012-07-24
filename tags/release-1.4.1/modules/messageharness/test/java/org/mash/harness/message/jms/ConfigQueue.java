package org.mash.harness.message.jms;

import javax.jms.Queue;
import javax.jms.JMSException;

/**
 *
 * @author teastlack
 * @since Jan 29, 2010 4:06:34 PM
 *
 */
public class ConfigQueue implements Queue
{
    private String queueName;

    public ConfigQueue(String queueName)
    {
        this.queueName = queueName;
    }

    public String getQueueName() throws JMSException
    {
        return queueName;
    }
}
