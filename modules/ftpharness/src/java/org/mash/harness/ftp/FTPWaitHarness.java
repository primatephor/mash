package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.HarnessContext;
import org.mash.harness.HarnessError;
import org.mash.harness.RunResponse;
import org.mash.harness.wait.PollingWaitHarness;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Poll an ftp directory of file until that file is present.  Basically just run a 'ls' command on an ftp server
 * at the specified path.  If there are files (or file), then polling is done.
 * <p/>
 * Configurations:
 * <ul>
 * <li> 'url' is the url to submit to </li>
 * <li> 'user' is the user to connect with</li>
 * <li> 'password' is the user's password </li>
 * <li> 'passive_mode' is the FTP client local passive mode (this is on my default)</li>
 * <li> 'timeout' optional time in milliseconds to stop polling (default timeout = 1 minute) </li>
 * <li> 'polltime' optional time in milliseconds to poll remote server (default poll time is 5 seconds) </li>
 * </ul>
 * <p/>
 * Parameters:
 * <ul>
 * <li> 'path' of file or directory to check </li>
 * <li> 'size' number of files to wait for, default is 1 </li>
 * <li> 'file_size' expected total size of all files</li>
 * </ul>
 *
 * @author
 * @since Oct 29, 2009 9:27:24 AM
 */
@HarnessName(name = "wait_ftp")
public class FTPWaitHarness extends PollingWaitHarness
{
    private static final Logger log = LogManager.getLogger(FTPWaitHarness.class.getName());
    private String user;
    private String password;
    private String url;
    private String path;
    private Boolean passiveMode = Boolean.TRUE;
    private String filenamePattern;

    //for lists
    private Long fileSize;
    private Integer size = 1;

    //for contents
    private Integer fileIndex;
    private List<String> fileContents;
    private String filename;

    private FTPRunHarness run;

    public boolean poll(HarnessContext context)
    {
        run = buildRunHarness();
        run.run(context);
        boolean result = false;
        RunResponse runResponse = getResponse();
        if (runResponse != null)
        {
            if (runResponse instanceof ListRunResponse)
            {
                ListRunResponse listResponse = (ListRunResponse) runResponse;
                if (fileSize != null)
                {
                    long totalSize = 0;
                    for (FTPFile file : listResponse.getFiles().values())
                    {
                        totalSize += file.getSize();
                    }
                    log.info("Found " + listResponse.getFiles().size() + " files, waiting for " + size);
                    log.info("Found " + totalSize + " bytes, waiting for " + fileSize);
                    result = listResponse.getFiles().size() >= size && totalSize >= fileSize;
                }
                else
                {
                    log.info("Found " + listResponse.getFiles().size() + " files, waiting for " + size);
                    result = listResponse.getFiles().size() >= size;
                }
            }
            else
            {
                //it's not null, so something matching was found
                result = true;
            }
        }
        else
        {
            log.debug("No response found in list");
        }
        return result;
    }

    protected FTPRunHarness buildRunHarness()
    {
        if (run == null)
        {
            if (isGetHarness())
            {
                GetHarness getHarness = new GetHarness();
                getHarness.setUrl(url);
                getHarness.setUser(user);
                getHarness.setPassword(password);
                getHarness.setPassiveMode(passiveMode.toString());
                getHarness.setPath(path);
                getHarness.setFileContents(fileContents);
                if (fileIndex != null)
                {
                    getHarness.setFileIndex(String.valueOf(fileIndex));
                }
                getHarness.setFilename(filename);
                getHarness.setFilenamePattern(filenamePattern);
                run = getHarness;
            }
            else
            {
                ListHarness listHarness = new ListHarness();
                listHarness.setUrl(url);
                listHarness.setPassword(password);
                listHarness.setUser(user);
                listHarness.setPath(path);
                listHarness.setPassiveMode(passiveMode.toString());
                listHarness.setFilenamePattern(filenamePattern);
                run = listHarness;
            }
        }
        return run;
    }

    public boolean isGetHarness()
    {
        return fileContents != null || fileIndex != null || filename != null;
    }

    protected HarnessError buildPollingFailureError()
    {
        HarnessError result;
        StringBuilder errorData = new StringBuilder();
        errorData.append("Timed out");
        if (isGetHarness())
        {
            errorData.append(" retrieving file");
        }
        else
        {
            errorData.append(" listing files");
        }
        if (path != null)
        {
            errorData.append(" on path ").append(path);
        }
        if (filename != null)
        {
            errorData.append(" with filename ").append(filename);
        }
        if (fileIndex != null)
        {
            errorData.append(" with index ").append(fileIndex);
        }
        if (fileContents != null && fileContents.size() > 0)
        {
            errorData.append(" containing data '");
            for (int i = 0; i < fileContents.size(); i++)
            {
                errorData.append(fileContents.get(i));
                if (i + 1 == fileContents.size())
                {
                    errorData.append(",");
                }
            }
            errorData.append("'");
        }
        result = new HarnessError(this, "FTP Polling Wait", errorData.toString());
        return result;
    }

    public RunResponse getResponse()
    {
        RunResponse response = null;
        if (run != null)
        {
            response = run.getResponse();
        }
        return response;
    }

    @HarnessConfiguration(name = "timeout")
    public void setTimeoutMillis(String timeoutMillis)
    {
        super.setTimeoutMillis(timeoutMillis);
    }

    @HarnessConfiguration(name = "polltime")
    public void setPollMillis(String pollMillis)
    {
        super.setPollMillis(pollMillis);
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

    @HarnessConfiguration(name = "url")
    public void setUrl(String url)
    {
        this.url = url;
    }

    @HarnessConfiguration(name = "passive_mode")
    public void setPassiveMode(String passiveMode) {
        this.passiveMode= Boolean.valueOf(passiveMode);
    }

    @HarnessParameter(name = "path")
    public void setPath(String path)
    {
        this.path = path;
    }

    @HarnessParameter(name = "size")
    public void setSize(String size)
    {
        this.size = Integer.valueOf(size);
    }

    @HarnessParameter(name = "file_size")
    public void setFileSize(String fileSize)
    {
        this.fileSize = Long.valueOf(fileSize);
    }

    @HarnessParameter(name = "file_index")
    public void setFileIndex(String fileIndex)
    {
        this.fileIndex = Integer.valueOf(fileIndex);
    }

    @HarnessParameter(name = "file_contents")
    public void setFileContents(String fileContents)
    {
        if (this.fileContents == null)
        {
            this.fileContents = new ArrayList<String>();
        }
        this.fileContents.add(fileContents);
    }

    @HarnessParameter(name = "file_name")
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    @HarnessParameter(name = "filename_pattern")
    public void setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
    }

}
