package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

import java.io.IOException;
import java.util.Collection;

/**
 * Perform an FTP delete.
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
 * <li> 'path' is the directory or file to delete</li>
 * </ul>
 *
 * @author
 * @since Dec 4, 2009 11:10:59 AM
 *
 */
@HarnessName(name = "delete_ftp")
public class DeleteHarness extends FTPRunHarness
{
    private static final Logger log = LogManager.getLogger(DeleteHarness.class.getName());
    private String path;
    private String filenamePattern;

    protected RunResponse runOperation(FTPClient client) throws OperationException
    {
        return delete(client, path);
    }

    public RunResponse delete(FTPClient client, String path) throws OperationException
    {
        RunResponse result;

        try
        {
            ListHarness list = new ListHarness();
            list.setPath(path);
            list.setFilenamePattern(filenamePattern);
            ListRunResponse response = (ListRunResponse) list.list(client);
            Collection<FTPFile> files = response.getFiles().values();

            //need to move to directory we just listed
            if (isDirectory(files, path))
            {
                if(!client.changeWorkingDirectory(path)){
                    throw new OperationException("Unable to move to directory "+path);
                }
            }

            for (FTPFile file : files)
            {
                log.info("Deleting " + file.getName());
                if(!client.deleteFile(file.getName())){
                    throw new OperationException("Unable to delete file "+path+"/"+file.getName());
                }
            }
            result = list.list(client);
        }
        catch (IOException e)
        {
            throw new OperationException("Problem deleting path "+path, e);
        }

        return result;
    }

    private boolean isDirectory(Collection<FTPFile> files, String ftpParams)
    {
        boolean result = false;
        if (files.size() > 1)
        {
            result = true;
        }
        else
        {
            if (files.size() > 0)
            {
                String fileName = files.iterator().next().getName();
                if (fileName != null && !fileName.equals(ftpParams))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    @HarnessParameter(name = "path")
    public void setPath(String path)
    {
        this.path = path;
    }

    @HarnessParameter(name = "filename_pattern")
    public void setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
    }
}
