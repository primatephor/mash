package org.mash.harness.jms;

import junit.framework.TestCase;

import javax.naming.NamingException;

import org.mash.harness.SendException;

/**
 *
 * @author teastlack
 * @since Jan 29, 2010 3:39:07 PM
 *
 */
public class TestXmlHarness extends TestCase
{
    public void testSend() throws NamingException, SendException
    {
        String message = "<base>" +
                         "  <child>data1</child>" +
                         "  <child2>data2</child2>" +
                         "</base>";
        String queueName = "theQueue";
        ConfigInitialContext context = new ConfigInitialContext();
        context.addData("/ConnectionFactory", new ConfigConnectionFactory(new ConfigConnection(new ConfigSession())));
        context.addData(queueName, new ConfigQueue(queueName));
        MyHarness harness = new MyHarness(new ConnectionData(context, queueName));
        harness.setMessage(message);
        harness.setProperties("prop1=value1");
        harness.setProperties("prop2=value2");
        harness.setAction("SEND");
        harness.setProviderUrl("theUrl");
        harness.setQueueName(queueName);

        harness.run(null, null);

    }

    private class MyHarness extends XmlJMSHarness
    {
        private ConnectionData connectionData;

        private MyHarness(ConnectionData connectionData)
        {
            this.connectionData = connectionData;
        }

        public JMSEndpointAdapter buildAdapter(String providerUrl, String queueName)
        {
            return new ConfigJMSAdapter(providerUrl,
                                        queueName,
                                        super.buildAdapter(providerUrl, queueName),
                                        connectionData);
        }
    }
}
