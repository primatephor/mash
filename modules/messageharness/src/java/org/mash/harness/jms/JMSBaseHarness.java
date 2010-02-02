package org.mash.harness.jms;

import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.Message;
import org.mash.harness.SendException;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Configurations:
 * <ul>
 * <li> 'provider_url' is the url of the jms connection provider </li>
 * <li> 'queue' is the queue name</li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters:
 * <ul>
 * <li> 'action' SEND/RECEIVE </li>
 * <li> 'property' is a little complex, since it's a name/value pair and needs to be parsed.  The format is
 * 'name'='value', so a value to a parameter would look like:
 * <code><Parameter name="property"><Value>myprop=somevalue</Value></Parameter></code>
 * </li>
 * </ul>
 *
 * @author teastlack
 * @since Jan 28, 2010 6:52:54 PM
 *
 */
public abstract class JMSBaseHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(JMSBaseHarness.class.getName());
    private String providerUrl;
    private String queueName;
    private ActionType action;
    private Map<String, String> properties = new HashMap<String, String>();

    protected Message message;

    protected enum ActionType
    {
        SEND,
        RECEIVE
    }

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        log.info("Connection provider:" + providerUrl + ", Queue:" + queueName);
        if (getErrors().size() == 0)
        {
            if (providerUrl == null)
            {
                getErrors().add(new HarnessError(this.getName(), "No provider url supplied"));
            }
            if (queueName == null)
            {
                getErrors().add(new HarnessError(this.getName(), "No queue name supplied"));
            }
            if (action == null)
            {
                getErrors().add(new HarnessError(this.getName(), "Action not set!"));
            }
        }

        if (getErrors().size() == 0)
        {
            log.info("Running action " + action);
            JMSEndpointAdapter endpoint = buildAdapter(providerUrl, queueName);
            if (ActionType.SEND.equals(action))
            {
                log.info("Sending to JMS queue " + queueName);
                try
                {
                    message = buildMessage();
                    message.getProperties().putAll(properties);
                    endpoint.send(message);
                }
                catch (SendException e)
                {
                    log.error("Problem sending message", e);
                    getErrors().add(new HarnessError(this.getName(), "Problem sending message", e));
                }
            }
            else if (ActionType.RECEIVE.equals(action))
            {
                log.info("Reading from JMS queue " + queueName);
                try
                {
                    message = endpoint.read();
                }
                catch (SendException e)
                {
                    log.error("Problem reading message", e);
                    getErrors().add(new HarnessError(this.getName(), "Problem reading message", e));
                }
            }
        }
        else
        {
            log.info("There were errors, not processing send / receive");
        }
    }

    public abstract JMSEndpointAdapter buildAdapter(String providerUrl, String queueName);

    public abstract Message buildMessage();

    public Message getMessage()
    {
        return message;
    }

    @HarnessConfiguration(name = "provider_url")
    public void setProviderUrl(String providerUrl)
    {
        this.providerUrl = providerUrl;
    }

    @HarnessConfiguration(name = "queue")
    public void setQueueName(String queueName)
    {
        this.queueName = queueName;
    }

    @HarnessConfiguration(name = "action")
    public void setAction(String action)
    {
        if (ActionType.SEND.name().equalsIgnoreCase(action))
        {
            this.action = ActionType.SEND;
        }
        else if (ActionType.RECEIVE.name().equalsIgnoreCase(action))
        {
            this.action = ActionType.RECEIVE;
        }
        else
        {
            getErrors().add(new HarnessError(this.getName(), "Invalid action (send or receive) type:" + action));
        }
    }

    @HarnessParameter(name = "property")
    public void setProperties(String nameValue)
    {
        if (nameValue != null)
        {
            String[] pair = nameValue.split("=");
            if (pair != null && pair.length > 0)
            {
                String name = pair[0];
                String value = null;
                if (pair.length > 1)
                {
                    value = pair[1];
                }
                properties.put(name, value);
            }
        }
    }
}

