package org.mash.harness.message.jms;

import junit.framework.TestCase;

import javax.naming.NamingException;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.mash.harness.message.SendException;
import org.jboss.mq.SpyTextMessage;

/**
 *
 * @author teastlack
 * @since Jan 29, 2010 3:39:07 PM
 *
 */
public class TestXmlHarness extends TestCase
{
    public void testSend() throws NamingException, SendException, JMSException
    {
        String message = "<base>" +
                         "  <child>data1</child>" +
                         "  <child2>data2</child2>" +
                         "</base>";
        String queueName = "theQueue";
        ConfigInitialContext context = new ConfigInitialContext();
        ConfigSession session = new ConfigSession(new ConfigProducer());
        context.addData("/ConnectionFactory", new ConfigConnectionFactory(new ConfigConnection(session)));
        context.addData(queueName, new ConfigQueue(queueName));
        MyJMSHarness harness = new MyJMSHarness(new ConnectionData(context, queueName));
        harness.setMessage(message);
        harness.setProperties("prop1=value1");
        harness.setProperties("prop2=value2");
        harness.setAction("SEND");
        harness.setProviderUrl("theUrl");
        harness.setQueueName(queueName);

        harness.run( null);
        ConfigProducer prod = (ConfigProducer) session.getSender();
        assertEquals("value1", prod.getMessage().getStringProperty("prop1"));
        assertEquals("value2", prod.getMessage().getStringProperty("prop2"));

        TextMessage msg = (TextMessage) prod.getMessage();
        assertEquals("<base>  <child>data1</child>  <child2>data2</child2></base>", msg.getText());

    }

    public void testReceive() throws NamingException, SendException, JMSException
    {
        String message = "<base>" +
                         "  <child>data1</child>" +
                         "  <child2>data2</child2>" +
                         "</base>";
        SpyTextMessage msg = new SpyTextMessage();
        msg.setText(message);
        msg.setStringProperty("prop1", "value1");
        msg.setStringProperty("prop2", "value2");

        String queueName = "theQueue";
        ConfigInitialContext context = new ConfigInitialContext();
        ConfigSession session = new ConfigSession(new ConfigConsumer(msg));
        context.addData("/ConnectionFactory", new ConfigConnectionFactory(new ConfigConnection(session)));
        context.addData(queueName, new ConfigQueue(queueName));
        MyJMSHarness harness = new MyJMSHarness(new ConnectionData(context, queueName));
        harness.setMessage(message);
        harness.setProperties("prop1=value1");
        harness.setProperties("prop2=value2");
        harness.setAction("RECEIVE");
        harness.setProviderUrl("theUrl");
        harness.setQueueName(queueName);

        harness.run( null);
        assertEquals("value1", harness.getResponse().getValue("prop1"));
        assertEquals("value2", harness.getResponse().getValue("prop2"));
        assertEquals("<base>  <child>data1</child>  <child2>data2</child2></base>", harness.getResponse().getString());

        assertEquals("data1", harness.getResponse().getValue("/base/child"));
    }

    private class MyJMSHarness extends XmlJMSHarness
    {
        private ConnectionData connectionData;

        private MyJMSHarness(ConnectionData connectionData)
        {
            this.connectionData = connectionData;
        }

        public JMSEndpointAdapter buildEndpoint()
        {
            JMSEndpointAdapter adapter = super.buildEndpoint();
            adapter.setEndpoint(new JMSEndpoint(connectionData));
            return adapter;
        }
    }
}
