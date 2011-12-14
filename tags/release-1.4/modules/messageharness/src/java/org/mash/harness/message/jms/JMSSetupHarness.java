package org.mash.harness.message.jms;

import org.mash.harness.SetupHarness;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.loader.HarnessConfiguration;
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
 * <li> 'action' CLEAN </li>
 * </ul>
 *
 * @author teastlack
 * @since Feb 1, 2010 7:18:26 PM
 *
 */
@HarnessName(name = "jms")
public class JMSSetupHarness extends BaseHarness implements SetupHarness
{
    private String providerUrl;
    private String queueName;
    private SetupType action;

    private enum SetupType
    {
        CLEAN
    }

    public void setup() throws Exception
    {
        if (getErrors().size() == 0)
        {
            if (providerUrl == null)
            {
                getErrors().add(new HarnessError(this, "Configuration", "No provider url supplied"));
            }
            if (queueName == null)
            {
                getErrors().add(new HarnessError(this, "Configuration", "No queue name supplied"));
            }
            if (action == null)
            {
                getErrors().add(new HarnessError(this, "Configuration", "Action not set!"));
            }
        }

        if (getErrors().size() == 0)
        {
            TextJMSHarness textHarness = new TextJMSHarness();
            textHarness.setAction("RECEIVE");
            textHarness.setProviderUrl(providerUrl);
            textHarness.setQueueName(queueName);

            textHarness.run(null, null);
            while (textHarness.getMessage() != null)
            {
                textHarness.run(null, null);
            }
            getErrors().addAll(textHarness.getErrors());
        }
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
        if (SetupType.CLEAN.name().equalsIgnoreCase(action))
        {
            this.action = SetupType.CLEAN;
        }
        else
        {
            getErrors().add(new HarnessError(this, "Configuration", "Invalid action type:" + action));
        }
    }

}
