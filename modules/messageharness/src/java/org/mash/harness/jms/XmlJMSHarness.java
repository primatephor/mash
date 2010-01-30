package org.mash.harness.jms;

import org.mash.harness.RunResponse;
import org.mash.harness.XmlResponse;
import org.mash.harness.MessagePropertyResponse;

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
 * <li> 'message' XML to send (if send is specified) </li>
 * </ul>
 *
 * @author teastlack
 * @since Jan 29, 2010 11:24:49 AM
 *
 */
public class XmlJMSHarness extends TextJMSHarness
{
    public RunResponse getResponse()
    {
        return new MessagePropertyResponse(new XmlResponse(String.valueOf(getMessage().getBody())),
                                           getMessage().getProperties());
    }
}
