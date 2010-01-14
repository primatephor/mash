package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.mash.file.ByteFileReader;
import org.mash.harness.HarnessError;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessParameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Perform an FTP get.
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
 * <li> 'path' is the directory to check</li>
 * <li> 'file_name' is the filname to get</li>
 * <li> 'output_file' is the path for get and put operations to retrieve the file or save it to</li>
 * <li> 'transfer_mode' Mode of transfer to perform, default is ASCII </li>
 * <ul>
 * <li> 'BINARY' </li>
 * <li> 'ASCII'</li>
 * </ul>
 * <li> 'file_index' the index (0-x) of the listed files to retrieve</li>
 * <li> 'file_contents' retrieve the file with these contents (EXPENSIVE!)</li>
 * </ul>
 *
 *
 * @author teastlack
 * @since Dec 4, 2009 9:49:20 AM
 *
 */
public class GetHarness extends FTPRunHarness
{
    private static final Logger log = Logger.getLogger(GetHarness.class.getName());

    private String output_file;
    private int transferMode = FTP.ASCII_FILE_TYPE;

    private Integer fileIndex;
    private String fileContents;
    private String filename;
    private String path;

    protected RunResponse runOperation(FTPClient client) throws OperationException
    {
        RunResponse result = null;
        try
        {
            if (filename != null)
            {
                log.info("Retrieving file " + filename);
                result = retrieve(client, filename);
            }
            else if (path != null)
            {
                if (fileContents != null)
                {
                    log.info("Looking for file in path " + path + " containing " + fileContents);
                    result = retrieveResponseContainment(client, path);
                }
                else if (fileIndex != null)
                {
                    log.info("Retriving file in " + path + " at index " + fileIndex);
                    result = retrieveIndexedResponse(client, path);
                }
            }
        }
        catch (Exception e)
        {
            throw new OperationException("Problem getting file", e);
        }
        return result;
    }

    protected RunResponse retrieve(FTPClient client, String path) throws OperationException
    {
        InputStream fileStream = null;
        RunResponse result;
        try
        {
            client.setFileType(transferMode);
            fileStream = client.retrieveFileStream(path);
            if (output_file == null)
            {
                String fileContents = new ByteFileReader().getContents(fileStream);
                result = new FileContentsResponse(fileContents);
            }
            else
            {
                File output = new File(output_file);
                OutputStream out = new FileOutputStream(output);
                byte buf[] = new byte[1024];
                int len;
                while ((len = fileStream.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }
                out.flush();
                out.close();
                result = new FileRefResponse(output);
            }

            if (!client.completePendingCommand())
            {
                throw new OperationException("File transfer incomplete!");
            }
        }
        catch (Exception e)
        {
            throw new OperationException("Error getting file:" + e.getMessage(), e);
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
                    throw new OperationException("Problem closing file", e);
                }
            }
        }
        return result;
    }

    private RunResponse retrieveResponseContainment(FTPClient client, String path) throws OperationException
    {
        RunResponse response = null;
        ListHarness listHarness = new ListHarness();
        listHarness.setPath(path);
        ListRunResponse ls = (ListRunResponse) listHarness.list(client);
        Map<String, FTPFile> files = ls.getFiles();

        log.info("Checking " + files.size() + " files");
        for (FTPFile ftpFile : files.values())
        {
            String filename = path + "/" + ftpFile.getName();
            log.info("Retrieving file " + filename);
            RunResponse result = retrieve(client, filename);
            String toCheck = result.getString();
            if (toCheck.contains(fileContents))
            {
                log.info("Found " + fileContents);
                log.debug("Response:" + toCheck);
                response = result;
                break;
            }
        }
        return response;
    }

    private RunResponse retrieveIndexedResponse(FTPClient client, String path) throws OperationException
    {
        RunResponse response = null;
        ListHarness listHarness = new ListHarness();
        listHarness.setPath(path);
        ListRunResponse ls = (ListRunResponse) listHarness.list(client);
        Map<String, FTPFile> files = ls.getFiles();
        List<FTPFile> fileList = new ArrayList<FTPFile>(files.values());
        Collections.sort(fileList, new FileDateComparator());
        if (fileList.size() <= fileIndex)
        {
            getErrors().add(new HarnessError(this.getName(),
                                             "Number of conversations found (" + fileList.size() + ") " +
                                             "is less than desired index (out of bounds) " + fileIndex));
        }
        else
        {
            FTPFile toRetrieve = fileList.get(fileIndex);
            String filename = path + "/" + toRetrieve.getName();
            log.info("Retrieving file " + filename);
            response = retrieve(client, filename);
        }
        return response;
    }


    @HarnessParameter(name = "output_file")
    public void setOutput_file(String output_file)
    {
        this.output_file = output_file;
    }

    @HarnessParameter(name = "transfer_mode")
    public void setTransferMode(String mode)
    {
        if ("BINARY".equalsIgnoreCase(mode))
        {
            this.transferMode = FTP.BINARY_FILE_TYPE;
        }
    }

    @HarnessParameter(name = "file_index")
    public void setFileIndex(String fileIndex)
    {
        this.fileIndex = Integer.valueOf(fileIndex);
    }

    @HarnessParameter(name = "file_contents")
    public void setFileContents(String fileContents)
    {
        this.fileContents = fileContents;
    }

    @HarnessParameter(name = "file_name")
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    @HarnessParameter(name = "path")
    public void setPath(String path)
    {
        this.path = path;
    }

    private class FileDateComparator implements Comparator<FTPFile>
    {
        public int compare(FTPFile o1, FTPFile o2)
        {
            return o1.getTimestamp().compareTo(o2.getTimestamp());
        }
    }
}
