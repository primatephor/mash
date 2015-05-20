package org.mash.harness.mail;

import org.mash.harness.StandardVerifyHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.HarnessError;
import org.mash.loader.HarnessConfiguration;
import org.apache.log4j.Logger;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

import java.util.List;
import java.util.ArrayList;

/**
 * Parameters names to validate:
 * <ul>
 * <li> subject (the subject of the email) </li>
 * <li> recipient (a list of recipients, you can have more than one) </li>
 * <li> sender (a list of senders, you can have more than one) </li>
 * <li> count (total number of messages in the folder) </li>
 * </ul>
 *
 * @author
 * @since Jul 26, 2010 2:25:19 PM
 */
@HarnessName(name = "email")
public class EmailVerifyHarness extends StandardVerifyHarness
{
    private static final Logger log = Logger.getLogger(EmailVerifyHarness.class.getName());
    private String subject;
    private List<String> recipients = new ArrayList<String>();
    private List<String> senders = new ArrayList<String>();
    private int count = -1;

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        if (run.getResponse() instanceof EmailResponse)
        {
            EmailResponse emailResponse = (EmailResponse) run.getResponse();
            if (count > 0)
            {
                if (count != emailResponse.getCount())
                {
                    getErrors().add(new HarnessError(this, "Verify Message Count",
                                                     "Expected count '" + count +
                                                     "' doesn't equal actual '" +
                                                     emailResponse.getCount() + "'"));
                }
            }
            if (subject != null)
            {
                if (!subject.equalsIgnoreCase(emailResponse.getSubject()))
                {
                    getErrors().add(new HarnessError(this, "Verify Message Subject",
                                                     "Expected subject '" + subject +
                                                     "' doesn't equal actual '" +
                                                     emailResponse.getSubject() + "'"));
                }
            }
            if (recipients.size() > 0)
            {
                for (String recipient : recipients)
                {
                    if (!emailResponse.getRecipients().contains(recipient))
                    {
                        getErrors().add(new HarnessError(this, "Verify Message Recipient",
                                                         "Expected recipient '" + recipient +
                                                         "' not present"));
                    }
                }
            }
            if (senders.size() > 0)
            {
                for (String sender : senders)
                {
                    if (!emailResponse.getSender().contains(sender))
                    {
                        getErrors().add(new HarnessError(this, "Verify Message Sender",
                                                         "Expected sender '" + sender +
                                                         "' not present"));
                    }
                }
            }
        }
        else
        {
            log.warn("Not verifying response, not an EmailResponse");
        }

        if (!hasErrors())
        {
            super.verify(run, setup);
        }
    }

    @HarnessParameter(name = "subject")
    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    @HarnessParameter(name = "recipient")
    public void setRecipients(String recipient)
    {
        this.recipients.add(recipient);
    }

    @HarnessParameter(name = "sender")
    public void setSender(String sender)
    {
        this.senders.add(sender);
    }

    @HarnessParameter(name = "count")
    public void setCount(String count)
    {
        this.count = Integer.valueOf(count);
    }
}
