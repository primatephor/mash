package org.mash.harness.ftp;

import org.apache.log4j.Logger;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.wait.PollingWaitHarness;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

import java.util.List;

/**
 * Poll an ftp directory of file until that file is present.  Basically just run a 'ls' command on an ftp server
 * at the specified path.  If there are files (or file), then polling is done.
 *
 * Configurations:
 * <ul>
 * <li> 'url' is the url to submit to </li>
 * <li> 'user' is the user to connect with</li>
 * <li> 'password' is the user's password </li>
 * <li> 'timeout' optional time in milliseconds to stop polling (default timeout = 1 minute) </li>
 * <li> 'polltime' optional time in milliseconds to poll remote server (default poll time is 5 seconds) </li>
 * </ul>
 *
 * Parameters:
 * <ul>
 * <li> 'path' of file or directory to check </li>
 * <li> 'size' number of files to wait for, default is 1 </li>
 * </ul>
 *
 * @author teastlack
 * @since Oct 29, 2009 9:27:24 AM
 *
 */
public class FTPWaitHarness extends PollingWaitHarness
{
    private static final Logger log = Logger.getLogger(FTPWaitHarness.class.getName());

    private String user;
    private String password;
    private String url;
    private String path;
    private Integer size = 1;

    private ListHarness run;

    public boolean poll(List<RunHarness> previous, List<SetupHarness> setups)
    {
        run = buildRunHarness();
        run.run(previous, setups);
        boolean result = false;
        ListRunResponse listResponse = (ListRunResponse) run.getResponse();
        if (listResponse != null)
        {
            log.info("Found " + listResponse.getFiles().size() + " files, waiting for " + size);
            if (listResponse.getFiles().size() >= size)
            {
                result = true;
            }
        }
        else
        {
            log.debug("No response found in list");
        }
        return result;
    }

    protected ListHarness buildRunHarness()
    {
        if (run == null)
        {
            run = new ListHarness();
            run.setUrl(url);
            run.setPassword(password);
            run.setUser(user);
            run.setPath(path);
        }
        return run;
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
}
