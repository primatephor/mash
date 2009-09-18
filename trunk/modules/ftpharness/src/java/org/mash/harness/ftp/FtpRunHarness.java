package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

import java.io.IOException;
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
    private String file;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        FTPClient client = new FTPClient();
        try
        {
            client.connect(url);
            client.login(user, password);
        }
        catch (IOException e)
        {
            log.error("Unexpected error connecting to ftp server", e);
            this.getErrors().add(new HarnessError(this.getClass().getName(),
                                                  "Unexpected error connecting to ftp server", e.getMessage()));
        }
    }

    public RunResponse getResponse()
    {
        throw new UnsupportedOperationException("Method getResponse not yet implemented");
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

    @HarnessParameter(name = "file")
    public void setFile(String file)
    {
        this.file = file;
    }
}
