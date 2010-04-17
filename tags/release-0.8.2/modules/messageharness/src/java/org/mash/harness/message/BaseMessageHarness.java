package org.mash.harness.message;

import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.HarnessError;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Configurations are dependent on implementations of this message harness
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
public abstract class BaseMessageHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(BaseMessageHarness.class.getName());
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
        if (getErrors().size() == 0)
        {
            if (action == null)
            {
                getErrors().add(new HarnessError(this, "Configuration", "Action not set!"));
            }
        }

        if (getErrors().size() == 0)
        {
            log.info("Running action " + action);
            MessageEndpoint endpoint = buildEndpoint();
            if (ActionType.SEND.equals(action))
            {
                try
                {
                    message = buildMessage();
                    message.getProperties().putAll(properties);
                    endpoint.send(message);
                }
                catch (SendException e)
                {
                    log.error("Problem sending message", e);
                    getErrors().add(new HarnessError(this, "Problem sending message", e));
                }
            }
            else if (ActionType.RECEIVE.equals(action))
            {
                try
                {
                    message = endpoint.read();
                    if (message != null)
                    {
                        log.debug("Pulled message:" + String.valueOf(getMessage().getBody()));
                    }
                }
                catch (SendException e)
                {
                    log.error("Problem reading message", e);
                    getErrors().add(new HarnessError(this, "Problem reading message", e));
                }
            }
        }
        else
        {
            log.info("There were errors, not processing send / receive");
        }
    }

    /**
     * Construct the enpoint in question.  This is an extension point, so implementations should be able to
     * get configuration information from this base harness.
     *
     * @return an endpoint to send messages to
     */
    public abstract MessageEndpoint buildEndpoint();

    /**
     * Given the data supplied via parameters, build a message appropriately.  For text messages, this may mean
     * pulling the data from a parameter, other messages may be more complex
     *
     * @return message to send
     */
    public abstract Message buildMessage();

    public Message getMessage()
    {
        return message;
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
            getErrors().add(new HarnessError(this, "Configuration", "Invalid action (send or receive) type:" + action));
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

