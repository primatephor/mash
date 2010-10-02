package org.mash.harness.mail;

import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.loader.HarnessConfiguration;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

/**
 * Provide common configuration settings for communicating with an email server
 *
 * Configurations:
 * <ul>
 * <li>smtp_server (the email server url)</li>
 * <li>user (the db username)</li>
 * <li>password (the db password)</li>
 * <li>folder (default is 'INBOX')</li>
 * </ul>
 * <p/>
 *
 * @author teastlack
 * @since Jul 23, 2010 5:10:33 PM
 */
public class IMAPEmailHarness extends BaseHarness
{
    private static final Logger log = Logger.getLogger(IMAPEmailHarness.class.getName());
    private String smtpServer;
    private String user;
    private String password;
    private String folder = "INBOX";
    private Provider provider = new Provider(Provider.Type.STORE,
                                             "imap",
                                             "com.sun.mail.imap.IMAPStore",
                                             "Sun Microsystems, Inc",
                                             "");

    protected Folder connect() throws MessagingException
    {
        Session mailSession = Session.getDefaultInstance(new Properties());
        if(log.isDebugEnabled())
        {
            mailSession.setDebug(true);
        }
        Store store = mailSession.getStore(provider);
        store.connect(getSmtpServer(), getUser(), getPassword());
        return store.getFolder(folder);
    }

    @HarnessConfiguration(name = "smtp_server")
    public void setSmtpServer(String smtpServer)
    {
        this.smtpServer = smtpServer;
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

    public String getFolder()
    {
        return folder;
    }

    @HarnessConfiguration(name = "folder")
    public void setFolder(String folder)
    {
        this.folder = folder;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUser()
    {
        return user;
    }

    public String getSmtpServer()
    {
        return smtpServer;
    }
}
