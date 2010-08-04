package org.mash.harness.mail;

import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessParameter;
import org.apache.log4j.Logger;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Flags;
import javax.mail.Address;
import javax.mail.MessagingException;

/**
 * IMPORTANT: not working.  DO NOT USE.
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
 * <li>address (the email address of recipient to run actions on)</li>
 * <li>action (what to do: CLEAN)</li>
 * </ul>
 * <p/>
 *
 * @author teastlack
 * @since Jul 23, 2010 5:09:47 PM
 */
public class IMAPSetupHarness extends IMAPEmailHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(IMAPSetupHarness.class.getName());
    private String emailAddress;
    private Action action;

    private enum Action
    {
        CLEAN
    }

    public void setup() throws Exception
    {
        Folder folder = connect();

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
            }
            Message[] deletedMsgs = folder.expunge();
            if (deletedMsgs != null)
            {
                log.info("Deleted " + deletedMsgs.length + " messages");
            }
            //expunge on close
            folder.close(true);
        }
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
