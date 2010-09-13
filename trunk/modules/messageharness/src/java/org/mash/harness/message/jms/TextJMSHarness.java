package org.mash.harness.message.jms;

import org.mash.harness.RawResponse;
import org.mash.harness.RunResponse;
import org.mash.harness.message.Message;
import org.mash.harness.message.MessagePropertyResponse;
import org.mash.loader.HarnessParameter;

/**
 * Configurations:
 * <ul>
 * <li> 'provider_url' is the url of the jms connection provider </li>
 * <li> 'queue' is the queue name</li>
 * </ul>
 * <p/>
 * Parameters:
 * <ul>
 * <li> 'action' SEND/RECEIVE </li>
 * <li> 'message' Text to send (if send is specified) </li>
 * <li> 'property' is a little complex, since it's a name/value pair and needs to be parsed.  The format is
 * 'name'='value', so a value to a parameter would look like:
 * <code><Parameter name="property"><Value>myprop=somevalue</Value></Parameter></code>
 * </ul>
 *
 * @author teastlack
 * @since Jan 28, 2010 6:23:21 PM
 *
 */
public class TextJMSHarness extends BaseJMSMessageHarness
{
    private String message;

    public JMSEndpointAdapter buildEndpoint()
    {
        return new TextJMSAdapter(getProviderUrl(), getQueueName());
    }

    public Message buildMessage()
    {
        Message result = new Message();
        result.setBody(message);
        return result;
    }

    public RunResponse getResponse()
    {
        RunResponse result = null;
        if (getMessage() != null)
        {
            return new MessagePropertyResponse(new RawResponse(String.valueOf(getMessage().getBody())),
                                               getMessage().getProperties());
        }
        return result;
    }

    @HarnessParameter(name = "message")
    public void setMessage(String message)
    {
        this.message = message;
    }
}
