
package org.mash.harness.db.sql;

import org.apache.log4j.Logger;
import org.mash.harness.HarnessContext;
import org.mash.harness.HarnessError;
import org.mash.harness.RunResponse;
import org.mash.harness.db.DBResult;
import org.mash.harness.wait.PollingWaitHarness;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

/**
 * Run an sql statement against the database, waiting for an expected number of results.  This wait harness ONLY counts
 * the result sets returned.
 * <p/>
 * <p/>
 * Necessary configurations are
 * <ul>
 * <li> 'url' the db url</li>
 * <li> 'user' the db username</li>
 * <li> 'password' the db password</li>
 * <li> 'driver' the db driver</li>
 * <li> 'timeout' optional time in milliseconds to stop polling (default timeout = 1 minute) </li>
 * <li> 'polltime' optional time in milliseconds to poll remote server (default poll time is 5 seconds) </li>
 * </ul>
 * <p/>
 * Parameters are:
 * <ul>
 * <li> 'sql' any sql to run </li>
 * <li> 'size' number of items to wait for in sql results (default is 1)</li>
 * </ul>
 *
 * @author
 * @since Sep 23, 2010 10:24:06 AM
 */
@HarnessName(name = "db_wait")
public class DBWaitHarness extends PollingWaitHarness
{
    private static final Logger log = Logger.getLogger(DBWaitHarness.class.getName());

    private String url;
    private String user;
    private String password;
    private String driver;
    private String sql;

    private Integer size = 1;

    private SQLRunHarness run;

    protected boolean poll(HarnessContext context)
    {
        boolean result = false;

        run = buildHarness();
        run.run(context);
        DBResult response = run.getResponse();

        if (response != null)
        {
            int responseSize = response.getResultSetData().size();
            if (responseSize == size)
            {
                log.info("Found all " + size + " records");
                result = true;
            }
            else
            {
                log.info("Found " + responseSize + " records");
            }
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

    protected SQLRunHarness buildHarness()
    {
        if (run == null)
        {
            run = new SQLRunHarness();
            run.setUrl(url);
            run.setPassword(password);
            run.setUser(user);
            run.setDriver(driver);
            run.setSql(sql);
        }
        return run;
    }

    protected HarnessError buildPollingFailureError()
    {
        return new HarnessError(this, "DB Polling Wait", "Timed out running SQL '" + sql + "'");
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

    @HarnessConfiguration(name = "driver")
    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    @HarnessParameter(name = "sql")
    public void setSql(String sql)
    {
        this.sql = sql;
    }

    @HarnessParameter(name = "size")
    public void setSize(String size)
    {
        this.size = Integer.valueOf(size);
    }
}
