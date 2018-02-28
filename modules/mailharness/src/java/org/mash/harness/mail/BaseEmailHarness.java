package org.mash.harness.mail;

import org.mash.harness.BaseHarness;
import org.mash.loader.HarnessConfiguration;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

/**
 * Provide common configuration settings for communicating with an email server
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
 *
 * @author
 * @since Jul 23, 2010 5:10:33 PM
 */
public class BaseEmailHarness extends BaseHarness
{
    private String mailServer;
    private int mailPort = -1;
    private String user;
    private String password;
    private String folderName = "INBOX";
    private String protocol = "imap";

    private Store store;
    private Folder folder;

    protected Store getStore() throws MessagingException
    {
        if (store == null || !store.isConnected())
        {
            Session mailSession = Session.getDefaultInstance(new Properties());
            store = mailSession.getStore(protocol);
            if (mailPort > 0)
            {
                store.connect(getMailServer(), mailPort, getUser(), getPassword());
            }
            else
            {
                store.connect(getMailServer(), getUser(), getPassword());
            }
        }
        return store;
    }

    protected Folder getFolder() throws MessagingException
    {
        if (folder == null || !folder.isOpen())
        {
            Store theStore = getStore();
            if (theStore != null)
            {
                folder = theStore.getFolder(folderName);
            }
        }
        return folder;
    }

    protected void close() throws MessagingException
    {
        if (folder != null)
        {
            folder.close(true);
        }

        if (store != null)
        {
            store.close();
        }
    }

    @HarnessConfiguration(name = "protocol")
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    @HarnessConfiguration(name = "mail_server")
    public void setMailServer(String mailServer)
    {
        String serverString = mailServer;
        int portIndex = serverString.lastIndexOf(':');
        if (portIndex > 0)
        {
            mailPort = Integer.valueOf(serverString.substring(portIndex + 1));
            serverString = serverString.substring(0, portIndex);
        }
        this.mailServer = serverString;
    }

    @HarnessConfiguration(name = "user")
    public void setUser(String user)
    {
        this.user = user;
    }

    @HarnessConfiguration(name = "password")
    public void setPassword(String password)
    {
        this.password = password;
    }

    @HarnessConfiguration(name = "folder")
    public void setFolder(String folder)
    {
        this.folderName = folder;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUser()
    {
        return user;
    }

    public String getMailServer()
    {
        return mailServer;
    }
}
