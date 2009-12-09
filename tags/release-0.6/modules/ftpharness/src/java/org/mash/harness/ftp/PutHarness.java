package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.mash.file.FileLoader;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessParameter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Perform an FTP put.
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
 * <li> 'output_path' is the directory to dump the file on</li> 
 * <li> 'file_name' is the local filename to put</li>
 * </ul>
 *
 * @author teastlack
 * @since Dec 4, 2009 11:20:37 AM
 *
 */
public class PutHarness extends FTPRunHarness
{
    private FileLoader loader = new FileLoader();
    private String file_name;
    private String output_path;
    private File path;

    protected RunResponse runOperation(FTPClient client) throws OperationException
    {
        path = getDefinition().getScriptDefinition().getPath();
        return put(client, file_name);
    }

    public RunResponse put(FTPClient client, String fileName) throws OperationException
    {
        InputStream fileStream = null;
        RunResponse result;
        if (fileName != null)
        {
            try
            {
                fileStream = loader.findStream(fileName, path);
                client.storeFile(output_path, fileStream);
            }
            catch (Exception e)
            {
                throw new OperationException("Problem putting file on server "+file_name, e);
            }
            finally
            {
                if (fileStream != null)
                {
                    try
                    {
                        fileStream.close();
                    }
                    catch (IOException e)
                    {
                        //yes, this could mask exceptions above
                        throw new OperationException("Problem reading file", e);
                    }
                }
            }
            //list the file as a response
            ListHarness listHarness = new ListHarness();
            listHarness.setPath(output_path);
            result = listHarness.list(client);

        }
        else
        {
            throw new OperationException("No file specified to read");
        }
        return result;
    }

    @HarnessParameter(name = "file_name")
    public void setFile_name(String file_name)
    {
        this.file_name = file_name;
    }

    @HarnessParameter(name = "output_path")
    public void setOutput_path(String output_path)
    {
        this.output_path = output_path;
    }
}
