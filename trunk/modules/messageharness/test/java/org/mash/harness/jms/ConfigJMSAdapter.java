package org.mash.harness.jms;

import org.mash.harness.Message;
import org.mash.harness.SendException;

/**
 *
 * @author teastlack
 * @since Jan 29, 2010 3:50:58 PM
 *
 */
public class ConfigJMSAdapter extends JMSEndpointAdapter
{
    private JMSEndpointAdapter target;
    private ConnectionData connectionData;

    public ConfigJMSAdapter(String providerUrl,
                            String queueName,
                            JMSEndpointAdapter target,
                            ConnectionData connectionData)
    {
        super(providerUrl, queueName);
        this.target = target;
        this.connectionData = connectionData;
    }

    public Message transform(javax.jms.Message jmsMessage) throws SendException
    {
        return target.transform(jmsMessage);
    }

    public javax.jms.Message transform(Message message) throws SendException
    {
        return target.transform(message);
    }

    public JMSEndpoint getEndpoint() throws SendException
    {
        return new JMSEndpoint(connectionData);
    }
}
