package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

import java.io.IOException;

/**
 * Perform an FTP list.
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
 * <li> 'path' is the path to run a list on</li>
 * </ul>
 *
 * @author
 * @since Dec 4, 2009 10:46:30 AM
 *
 */
@HarnessName(name = "list_ftp")
public class ListHarness extends FTPRunHarness
{
    private static final Logger log = Logger.getLogger(ListHarness.class.getName());
    private String path;

    protected RunResponse runOperation(FTPClient client) throws OperationException
    {
        return list(client);
    }

    public RunResponse list(FTPClient client) throws OperationException
    {
        RunResponse result;
        try
        {
            FTPFile[] files;
            if (path != null)
            {
                log.info("List on path "+path);
                files = client.listFiles(path);
            }
            else
            {
                files = client.listFiles();
            }
            result = new ListRunResponse(files);
        }
        catch (IOException e)
        {
            throw new OperationException("Problem listing files in path", e);
        }

        return result;
    }

    @HarnessParameter(name = "path")
    public void setPath(String path)
    {
        this.path = path;
    }
}
