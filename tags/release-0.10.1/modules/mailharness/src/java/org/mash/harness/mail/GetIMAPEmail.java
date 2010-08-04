package org.mash.harness.mail;

import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.HarnessError;
import org.mash.loader.HarnessParameter;
import org.apache.log4j.Logger;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Address;
import javax.mail.MessagingException;
import java.util.List;

/**
 *
 * Configurations:
 * <ul>
 * <li>smtp_server (the email server url)</li>
 * <li>user (the db username)</li>
 * <li>password (the db password)</li>
 * <li>folder (default is 'INBOX')</li>
 * </ul>
 * <p/>
 * Parameters:
 * <ul>
 * <li>message_number (which message to get, default = 1)</li>
 * <li>address (which address to get.  If nothing is specified, gets all)</li>
 * </ul>
 *
 * Message number is defaulted to 1.  If an address is specified, then the email retrieved is the first message
 * for that address.  If no address is specified, then the first email found is retrieved.
 *
 * @author teastlack
 * @since Jul 23, 2010 5:09:07 PM
 */
public class GetIMAPEmail extends IMAPEmailHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(GetIMAPEmail.class.getName());

    private int messageNumber = 1;
    private String address;
    private EmailResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        log.info("Running EmailHarness");
        try
        {
            Folder folder = connect();
            folder.open(Folder.READ_ONLY);
            Message message = null;
            if (address == null)
            {
                message = folder.getMessage(messageNumber);
            }
            else
            {
                log.info("Looking for address " + address);
                int count = folder.getMessageCount();
                int addyCount = 1;
                for (int i = 1; i <= count; i++)
                {
                    Message toCheck = folder.getMessage(i);
                    if (isValidAddress(toCheck))
                    {
                        log.debug("Checking message for " + address);
                        if (messageNumber == addyCount)
                        {
                            log.debug("Found the message");
                            message = toCheck;
                            break;
                        }
                        else
                        {
                            addyCount++;
                        }
                    }
                }
            }
            if (message != null)
            {
                response = new EmailResponse(message, folder.getMessageCount());
            }
        }
        catch (Exception e)
        {
            log.error("Unexpected error", e);
            getErrors().add(new HarnessError(this, "Error", e.getMessage()));
        }
    }

    private boolean isValidAddress(Message message) throws MessagingException
    {
        boolean result = false;
        for (Address recipient : message.getAllRecipients())
        {
            if (address.equalsIgnoreCase(recipient.toString()))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public RunResponse getResponse()
    {
        return response;
    }

    @HarnessParameter(name = "message_number")
    public void setMessageNumber(String messageNumber)
    {
        this.messageNumber = Integer.valueOf(messageNumber);
    }

    @HarnessParameter(name = "address")
    public void setAddress(String address)
    {
        this.address = address;
    }
}
