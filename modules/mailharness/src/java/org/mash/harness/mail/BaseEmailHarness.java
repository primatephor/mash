package org.mash.harness.mail;

import org.mash.harness.BaseHarness;
import org.mash.loader.HarnessConfiguration;

/**
 * Provide common configuration settings for communicating with an email server
 *
 * Configurations:
 * <ul>
 * <li>smtp_server (the email server url)</li>
 * <li>user (the db username)</li>
 * <li>password (the db password)</li>
 * </ul>
 * <p/>
 *
 * @author teastlack
 * @since Jul 23, 2010 5:10:33 PM
 */
public class BaseEmailHarness extends BaseHarness
{
    private String smtpServer;
    private String user;
    private String password;

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
