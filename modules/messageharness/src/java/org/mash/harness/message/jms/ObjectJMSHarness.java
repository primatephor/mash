package org.mash.harness.message.jms;

import org.mash.harness.message.Message;
import org.mash.harness.RunResponse;
import org.mash.harness.OGNLResponse;
import org.mash.harness.HarnessError;
import org.mash.harness.message.MessagePropertyResponse;
import org.mash.loader.HarnessConfiguration;

/**
 * Configurations:
 * <ul>
 * <li> 'provider_url' is the url of the jms connection provider </li>
 * <li> 'queue' is the queue name</li>
 * </ul>
 * <p/>
 * Parameters:
 * <ul>
 * <li> 'action' RECEIVE (send is currently unavailable) </li>
 * </ul>
 *
 * @author teastlack
 * @since Jan 29, 2010 11:27:13 AM
 *
 */
public class ObjectJMSHarness extends BaseJMSMessageHarness
{
    public JMSEndpointAdapter buildEndpoint()
    {
        return new ObjectJMSAdapter(getProviderUrl(), getQueueName());
    }

    public Message buildMessage()
    {
        throw new UnsupportedOperationException("Unable to build a send message for object messages right now!");
    }

    public RunResponse getResponse()
    {
        return new MessagePropertyResponse(new OGNLResponse(getMessage().getBody()),
                                           getMessage().getProperties());
    }

    @HarnessConfiguration(name = "action")
    public void setAction(String action)
    {
        if (!ActionType.RECEIVE.name().equalsIgnoreCase(action))
        {
            getErrors().add(new HarnessError(this.getName(), "Invalid action type:" + action));
        }
        super.setAction(action);
    }
}
