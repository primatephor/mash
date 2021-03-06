package org.mash.harness.message.jms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.HarnessContext;
import org.mash.harness.message.BaseMessageHarness;
import org.mash.harness.HarnessError;
import org.mash.loader.HarnessConfiguration;

/**
 * Configurations:
 * <ul>
 * <li> 'provider_url' is the url of the jms connection provider </li>
 * <li> 'queue' is the queue name</li>
 * </ul>
 *
 * @author
 * @since Feb 5, 2010 9:20:18 AM
 *
 */
public abstract class BaseJMSMessageHarness extends BaseMessageHarness
{
    private static final Logger log = LogManager.getLogger(BaseJMSMessageHarness.class.getName());
    private String providerUrl;
    private String queueName;

    public void run(HarnessContext context)
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

        super.run(context);
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
