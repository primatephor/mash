package org.mash.harness.ftp;

import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

import java.util.Date;
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
 * <li> 'timeout' optional time in milliseconds to stop polling </li>
 * <li> 'polltime' optional time in milliseconds to poll remote server</li>
 * </ul>
 *
 * Parameters:
 * <ul>
 * <li> 'path' of file or directory to check </li>
 * </ul>
 *
 * @author teastlack
 * @since Oct 29, 2009 9:27:24 AM
 *
 */
public class FTPWaitHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(FTPWaitHarness.class.getName());
    //timeout after 1 minute by default
    private Integer timeoutMillis = 60 * 1000;
    //poll every 5 seconds by default
    private Integer pollMillis = 5 * 1000;

    private String user;
    private String password;
    private String url;
    private String path;

    private FTPRunHarness run;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        run = buildRunHarness();
        run.setUrl(url);
        run.setPassword(password);
        run.setUser(user);
        run.setFtpParams(path);
        run.setOperation(FTPOperations.LS.name());

        long start = new Date().getTime();
        long current = 0l;
        boolean isComplete = false;
        while (current < timeoutMillis && !isComplete)
        {
            log.info("Polling remote path '" + path + "'");
            run.run(previous, setups);
            isComplete = isDoneWaiting(run.getResponse());
            if (!isComplete)
            {
                long timeToWait = pollMillis;
                if (current + pollMillis > timeoutMillis)
                {
                    //end at timeout then, plus a little slop
                    timeToWait = current + pollMillis - timeoutMillis +500;
                }

                try
                {
                    Thread.sleep(timeToWait);
                }
                catch (InterruptedException e)
                {
                    this.getErrors().add(new HarnessError(this.getClass().getName(), "Problem waiting for next polling", e.getMessage()));
                }
            }
            current = new Date().getTime() - start;
        }

        if (!isComplete)
        {
            this.getErrors().add(new HarnessError(this.getClass().getName(), "Timed out before found " + path));
        }
    }

    protected FTPRunHarness buildRunHarness()
    {
        return new FTPRunHarness();
    }

    private boolean isDoneWaiting(RunResponse response)
    {
        ListRunResponse listResponse = (ListRunResponse) response;
        boolean result = false;
        if (listResponse.getFiles().size() > 0)
        {
            result = true;
        }
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
        this.timeoutMillis = Integer.valueOf(timeoutMillis);
    }

    @HarnessConfiguration(name = "polltime")
    public void setPollMillis(String pollMillis)
    {
        this.pollMillis = Integer.valueOf(pollMillis);
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
}
