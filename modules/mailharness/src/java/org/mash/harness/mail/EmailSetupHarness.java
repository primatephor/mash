package org.mash.harness.mail;

import org.apache.log4j.Logger;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessParameter;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * IMPORTANT: not working.  DO NOT USE.
 * <p/>
 * Configurations:
 * <ul>
 * <li>protocol (imap, imaps, pop, etc)</li>
 * <li>smtp_server (the email server url)</li>
 * <li>user (the db username)</li>
 * <li>password (the db password)</li>
 * <li>folder (default is 'INBOX')</li>
 * </ul>
 * <p/>
 * Parameters:
 * <ul>
 * <li>address (the email address of recipient to run actions on)</li>
 * <li>action (what to do: CLEAN)</li>
 * </ul>
 * <p/>
 *
 * @author teastlack
 * @since Jul 23, 2010 5:09:47 PM
 */
public class EmailSetupHarness extends BaseEmailHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(EmailSetupHarness.class.getName());
    private String emailAddress;
    private Action action;

    private enum Action
    {
        CLEAN
    }

    public void setup() throws Exception
    {
        Folder folder = getFolder();
        if (Action.CLEAN.equals(action))
        {
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            for (Message message : messages)
            {
                if (shouldActOn(message))
                {
                    log.info("Deleting message:" + message.getMessageNumber());
                    message.setFlag(Flags.Flag.DELETED, true);
                }
                else
                {
                    StringBuffer recips = null;
                    for (Address address : message.getAllRecipients())
                    {
                        if (recips == null)
                        {
                            recips = new StringBuffer();
                        }
                        else
                        {
                            recips.append(",");
                        }
                        recips.append(address);
                    }
                    log.info("Not cleaning message:" + message.getSubject() + " for users:" + recips);
                }
            }
        }
        close();
    }

    protected boolean shouldActOn(Message message) throws MessagingException
    {
        Boolean result = false;
        if (emailAddress != null)
        {
            for (Address address : message.getAllRecipients())
            {
                if (emailAddress.equalsIgnoreCase(address.toString()))
                {
                    result = true;
                    break;
                }
            }
        }
        else
        {
            result = true;
        }
        return result;
    }

    @HarnessParameter(name = "address")
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    @HarnessParameter(name = "action")
    public void setAction(String action)
    {
        this.action = Action.valueOf(action);
    }
}
