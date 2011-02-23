package org.mash.harness.mail;

import org.apache.log4j.Logger;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessParameter;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.List;

/**
 * Configurations:
 * <ul>
 * <li>protocol (imap, pop3, etc)</li>
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
 * <p/>
 * Message number is defaulted to 1.  If an address is specified, then the email retrieved is the first message
 * for that address.  If no address is specified, then the first email found is retrieved.
 *
 * @author teastlack
 * @since Jul 23, 2010 5:09:07 PM
 */
public class GetEmail extends BaseEmailHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(GetEmail.class.getName());

    private int messageNumber = 1;
    private String address;
    private String subject;
    private EmailResponse response;

    public void run(List<RunHarness> previous,
                    List<SetupHarness> setups)
    {
        log.info("Running GetEmail");
        try
        {
            Folder folder = getFolder();
            folder.open(Folder.READ_ONLY);
            Message message = null;
            int totalMessages = 0;
            if (address == null)
            {
                log.info("Retrieving message number " + messageNumber);
                message = folder.getMessage(messageNumber);
                totalMessages = folder.getMessageCount();
            }
            else
            {
                log.info("Looking for address " + address);
                int addyCount = 1;
                Message[] toCheck = folder.getMessages();
                if (toCheck != null)
                {
                    for (Message check : toCheck)
                    {
                        if (isValid(check))
                        {
                            if (messageNumber == addyCount)
                            {
                                message = check;
                            }
                            addyCount++;
                            totalMessages++;
                        }
                    }
                }
                else
                {
                    log.warn("No messages in folder " + folder.getFullName());
                }
            }
            if (message != null)
            {
                log.info("Found the message with subject:" + message.getSubject());
                response = new EmailResponse(message, totalMessages);
            }
            close();
        }
        catch (Exception e)
        {
            log.error("Unexpected error", e);
            getErrors().add(new HarnessError(this, "Error", e.getMessage()));
        }
    }

    protected boolean isValid(Message message) throws MessagingException
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
        if (result)
        {
            if (subject != null)
            {
                if (!subject.equalsIgnoreCase(message.getSubject()))
                {
                    log.debug("subject '" + message.getSubject() +
                            "' does not equal expected subject '" + subject + "'");
                    result = false;
                }
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

    @HarnessParameter(name = "subject")
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
}