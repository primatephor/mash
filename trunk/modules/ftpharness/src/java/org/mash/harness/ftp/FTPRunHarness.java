package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.ftp.operations.DeleteOperation;
import org.mash.harness.ftp.operations.GetOperation;
import org.mash.harness.ftp.operations.ListOperation;
import org.mash.harness.ftp.operations.PutOperation;
import org.mash.harness.ftp.operations.RawOperation;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

import java.util.List;

/**
 * Perform basic FTP operations on a server.
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
 * <li> 'operation' to perform on the server </li>
 * <ul>
 * <li> 'GET' retrieve a file from server </li>
 * <li> 'PUT' puts a file on server </li>
 * <li> 'LS' lists files on server</li>
 * </ul>
 * <li> 'ftp_params' parameters to apply to the operation </li>
 * <li> 'file_path' is the path for get and put operations to retrieve the file or save it to</li>
 * <li> 'transfer_mode' Mode of transfer to perform, default is ASCII </li>
 * <ul>
 * <li> 'BINARY' </li>
 * <li> 'ASCII'</li>
 * </ul>
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
    private FTPOperations operation;
    private String ftpParams;
    private String fileName;
    private int transferMode = FTP.ASCII_FILE_TYPE;

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
                    client.setFileType(transferMode);
                    runOperation(client, operation);
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

    protected FTPClient buildClient()
    {
        return new FTPClient();
    }

    private void runOperation(FTPClient client, FTPOperations operation)
            throws Exception
    {
        if (operation != null)
        {
            if (FTPOperations.LS.equals(operation))
            {
                response = new ListOperation().operate(client, ftpParams);
            }
            else if (FTPOperations.GET.equals(operation))
            {
                response = new GetOperation(fileName).operate(client, ftpParams);
            }
            else if (FTPOperations.PUT.equals(operation))
            {
                response = new PutOperation(fileName, getDefinition()).operate(client, ftpParams);
            }
            else if (FTPOperations.DELETE.equals(operation))
            {
                response = new DeleteOperation().operate(client, ftpParams);
            }
            else if (FTPOperations.RAW.equals(operation))
            {
                response = new RawOperation().operate(client, ftpParams);
            }
        }
        else
        {
            this.getErrors().add(new HarnessError(this.getClass().getName(), "No Operation Specified!"));
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

    @HarnessParameter(name = "ftp_params")
    public void setFtpParams(String ftpParams)
    {
        this.ftpParams = ftpParams;
    }

    @HarnessParameter(name = "transfer_mode")
    public void setTransferMode(String mode)
    {
        if ("BINARY".equalsIgnoreCase(mode))
        {
            this.transferMode = FTP.BINARY_FILE_TYPE;
        }
    }

    @HarnessParameter(name = "file_path")
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
