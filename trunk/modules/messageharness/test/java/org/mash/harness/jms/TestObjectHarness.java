package org.mash.harness.jms;

import junit.framework.TestCase;

import javax.naming.NamingException;
import javax.jms.JMSException;

import org.mash.harness.SendException;
import org.jboss.mq.SpyObjectMessage;

import java.io.Serializable;

/**
 *
 * @author teastlack
 * @since Feb 1, 2010 10:33:39 AM
 *
 */
public class TestObjectHarness extends TestCase
{
    public void testSend() throws NamingException, SendException, JMSException
    {
        String queueName = "theQueue";
        ConfigInitialContext context = new ConfigInitialContext();
        ConfigSession session = new ConfigSession(new ConfigProducer());
        context.addData("/ConnectionFactory", new ConfigConnectionFactory(new ConfigConnection(session)));
        context.addData(queueName, new ConfigQueue(queueName));
        MyHarness harness = new MyHarness(new ConnectionData(context, queueName));
        harness.setProperties("prop1=value1");
        harness.setProperties("prop2=value2");
        harness.setAction("SEND");
        harness.setProviderUrl("theUrl");
        harness.setQueueName(queueName);

        harness.run(null, null);

        //should have failed, as send is invalid for object messages right now
        assertEquals(1, harness.getErrors().size());
        assertEquals("Invalid action type:SEND", harness.getErrors().get(0).getValue());
    }

    public void testReceive() throws NamingException, SendException, JMSException
    {
        Serializable message = new MyObjectMessage("child","data1");
        SpyObjectMessage msg = new SpyObjectMessage();
        msg.setObject(message);
        msg.setStringProperty("prop1", "value1");
        msg.setStringProperty("prop2", "value2");

        String queueName = "theQueue";
        ConfigInitialContext context = new ConfigInitialContext();
        ConfigSession session = new ConfigSession(new ConfigConsumer(msg));
        context.addData("/ConnectionFactory", new ConfigConnectionFactory(new ConfigConnection(session)));
        context.addData(queueName, new ConfigQueue(queueName));
        MyHarness harness = new MyHarness(new ConnectionData(context, queueName));
        harness.setAction("RECEIVE");
        harness.setProviderUrl("theUrl");
        harness.setQueueName(queueName);

        harness.run(null, null);
        assertEquals("value1", harness.getResponse().getValue("prop1"));
        assertEquals("value2", harness.getResponse().getValue("prop2"));
        assertEquals("child", harness.getResponse().getValue("name"));
        assertEquals("data1", harness.getResponse().getValue("value"));
    }

    private class MyHarness extends ObjectJMSHarness
    {
        private ConnectionData connectionData;

        private MyHarness(ConnectionData connectionData)
        {
            this.connectionData = connectionData;
        }

        public JMSEndpointAdapter buildAdapter(String providerUrl, String queueName)
        {
            JMSEndpointAdapter adapter = super.buildAdapter(providerUrl, queueName);
            adapter.setEndpoint(new JMSEndpoint(connectionData));
            return adapter;
        }
    }
}

