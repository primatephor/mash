package org.mash.harness.jms;

import org.mash.harness.RunResponse;
import org.mash.harness.Message;
import org.mash.harness.StringResponse;
import org.mash.harness.MessagePropertyResponse;
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
 * </ul>
 *
 * @author teastlack
 * @since Jan 28, 2010 6:23:21 PM
 *
 */
public class TextJMSHarness extends JMSBaseHarness
{
    private String message;

    public JMSEndpointAdapter buildAdapter(String providerUrl, String queueName)
    {
        return new TextJMSAdapter(providerUrl, queueName);
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
            return new MessagePropertyResponse(new StringResponse(String.valueOf(getMessage().getBody())),
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
