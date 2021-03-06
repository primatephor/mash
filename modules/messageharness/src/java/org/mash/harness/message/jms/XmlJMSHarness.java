package org.mash.harness.message.jms;

import org.mash.harness.RunResponse;
import org.mash.harness.XmlResponse;
import org.mash.harness.message.MessagePropertyResponse;
import org.mash.loader.HarnessName;

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
 * <li> 'property' is a little complex, since it's a name/value pair and needs to be parsed.  The format is
 * 'name'='value', so a value to a parameter would look like:
 * <code><Parameter name="property"><Value>myprop=somevalue</Value></Parameter></code>
 * </ul>
 *
 * @author
 * @since Jan 29, 2010 11:24:49 AM
 *
 */
@HarnessName(name = "xml_jms")
public class XmlJMSHarness extends TextJMSHarness
{
    public RunResponse getResponse()
    {
        RunResponse result = null;
        if (getMessage() != null)
        {
            return new MessagePropertyResponse(new XmlResponse(String.valueOf(getMessage().getBody())),
                                               getMessage().getProperties());
        }
        return result;
    }
}
