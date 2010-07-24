package org.mash.harness.mail;

import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessParameter;
import org.apache.log4j.Logger;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Message;
import java.util.Properties;

/**
 * IMPORTANT: not working.  DO NOT USE.
 *
 * Configurations:
 * <ul>
 * <li>smtp_server (the email server url)</li>
 * <li>user (the db username)</li>
 * <li>password (the db password)</li>
 * </ul>
 * <p/>
 * Parameters:
 *
 * @author teastlack
 * @since Jul 23, 2010 5:09:47 PM
 */
public class EmailSetupHarness extends BaseEmailHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(EmailSetupHarness.class.getName());
    private String emailAddress;
    private String folderName;
    private Action action;

    private enum Action
    {
        CLEAN
    }

    public void setup() throws Exception
    {
        Session mailSession = Session.getDefaultInstance(new Properties());
        Store store = mailSession.getStore();

        Folder folder;
        if (folderName != null)
        {
            folder = store.getFolder(folderName);
        }
        else
        {
            folder = store.getDefaultFolder();
        }

        Message[] messages = folder.expunge();
        for (Message message : messages)
        {
            log.info("Deleting message with subject:" + message.getSubject() + " from " + message.getFrom()[0]);
            log.trace("Message:" + message.getContent());
        }
    }

    @HarnessParameter(name = "address")
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    @HarnessParameter(name = "folder")
    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
    }

    @HarnessParameter(name = "action")
    public void setAction(String action)
    {
        this.action = Action.valueOf(action);
    }
}
