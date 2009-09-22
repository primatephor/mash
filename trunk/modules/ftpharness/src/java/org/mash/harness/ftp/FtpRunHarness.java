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
 *
 * @author teastlack
 * @since Sep 17, 2009 10:20:40 AM
 *
 */
public class FtpRunHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(FtpRunHarness.class.getName());
    //configs
    private String url;
    private String user;
    private String password;

    //params
    private FTPOperations operation;
    private String ftpParams;

    private RunResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        FTPClient client = new FTPClient();
        try
        {
            int reply;
            client.connect(url);
            log.info("Connected to " + url + ".");

            // After connection attempt, you should check the reply code to verify success.
            reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                client.disconnect();
                log.error("FTP connection refused");
                this.getErrors().add(new HarnessError(this.getClass().getName(), "FTP connection refused"));
            }
            client.login(user, password);
            if (operation != null)
            {
                FTPOperation runOp = operation.getOperation();
                if (runOp != null)
                {
                    response = runOp.operate(client, ftpParams);
                }
            }

            client.logout();
        }
        catch (Exception e)
        {
            log.error("Unexpected error connecting to ftp server", e);
            this.getErrors().add(new HarnessError(this.getClass().getName(),
                                                  "Unexpected error connecting to ftp server", e.getMessage()));
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

    @HarnessParameter(name = "operation")
    public void setOperation(String op)
    {
        op = op.toUpperCase();
        this.operation = FTPOperations.valueOf(op);
    }

    @HarnessParameter(name = "ftpParams")
    public void setFtpParams(String ftpParams)
    {
        this.ftpParams = ftpParams;
    }
}
