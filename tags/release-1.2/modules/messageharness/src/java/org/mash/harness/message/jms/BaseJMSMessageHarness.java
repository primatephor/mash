package org.mash.harness.message.jms;

import org.mash.harness.message.BaseMessageHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.HarnessError;
import org.mash.loader.HarnessConfiguration;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Configurations:
 * <ul>
 * <li> 'provider_url' is the url of the jms connection provider </li>
 * <li> 'queue' is the queue name</li>
 * </ul>
 *
 * @author teastlack
 * @since Feb 5, 2010 9:20:18 AM
 *
 */
public abstract class BaseJMSMessageHarness extends BaseMessageHarness
{
    private static final Logger log = Logger.getLogger(BaseJMSMessageHarness.class.getName());
    private String providerUrl;
    private String queueName;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        log.info("Connection provider:" + providerUrl + ", Queue:" + queueName);
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
        }

        super.run(previous, setups);
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

    public String getProviderUrl()
    {
        return providerUrl;
    }

    public String getQueueName()
    {
        return queueName;
    }
}
