package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

import java.util.List;

/**
 * Perform generic FTP operations on a server, based on whatever parameters are passed via the ftp_params param.
 *
 * Configurations:
 * <ul>
 * <li> 'url' is the url to submit to </li>
 * <li> 'user' is the user to connect with</li>
 * <li> 'password' is the user's password </li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters are applied to the request, and the request is invoked.  There are special parameters:
 * <ul>
 * <li> 'ftp_params' parameters to apply to the operation </li>
 * </ul>
 *
 * @author teastlack
 * @since Sep 17, 2009 10:20:40 AM
 *
 */
public class FTPRunHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(FTPRunHarness.class.getName());
    //configs
    private String url;
    private String user;
    private String password;

    //params
    private String ftpParams;

    private RunResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        FTPClient client = buildClient();
        try
        {
            log.info("Attempting connect to " + url + ".");
            client.connect(url);
            int reply = client.getReplyCode();
            log.info("Connection status: " + reply);

            // After connection attempt, you should check the reply code to verify success.
            if (!FTPReply.isPositiveCompletion(reply))
            {
                client.disconnect();
                log.error("FTP connection refused");
                this.getErrors().add(new HarnessError(this.getClass().getName(), "FTP connection refused"));
            }

            if (!hasErrors())
            {
                if (!client.login(user, password))
                {
                    this.getErrors().add(new HarnessError(this.getClass().getName(), "Unable to login with user " + user));
                }
                else
                {
                    response = runOperation(client);
                }
                client.logout();
            }
        }
        catch (Exception e)
        {
            log.error("Unexpected error ftping server", e);
            this.getErrors().add(new HarnessError(this.getClass().getName(),
                                                  "Unexpected error ftping server", e.getMessage()));
        }
        finally
        {
            if (client.isConnected())
            {
                try
                {
                    client.disconnect();
                }
                catch (Exception ioe)
                {
                    log.error("Unexpected error closing connection to ftp server", ioe);
                }
            }
        }
    }

    protected RunResponse runOperation(FTPClient client) throws OperationException
    {
        RunResponse result = null;
        try
        {
            if (ftpParams != null)
            {
                String command = ftpParams;
                String args = null;
                if (command.indexOf(" ") > 0)
                {
                    command = command.substring(0, command.indexOf(" "));
                    args = ftpParams.substring(ftpParams.indexOf(" ") + 1);
                }
                client.sendCommand(command, args);
                result = new RawResponse(client.getReplyStrings());
            }
        }
        catch (Exception e)
        {
            throw new OperationException("Problem running command remotely", e);
        }
        return result;
    }

    protected FTPClient buildClient()
    {
        return new FTPClient();
    }

    public RunResponse getResponse()
    {
        return response;
    }

    @HarnessConfiguration(name = "url")
    public void setUrl(String url)
    {
        this.url = url;
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

    @HarnessParameter(name = "ftp_params")
    public void setFtpParams(String ftpParams)
    {
        this.ftpParams = ftpParams;
    }

}