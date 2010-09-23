/*******************************************************************************
 * Copyright (c) 2010 Ensenda, Inc. All Rights Reserved.
 * This code  is the  sole  property  of  Ensenda Inc.,
 * and is protected  by  copyright  under the  laws of the United
 * States. This program is confidential, proprietary, and a trade
 * secret, not to be disclosed without written authorization from
 * Ensenda Inc.  Any  use, duplication, or  disclosure
 * of  this  program  by other than Ensenda Inc. and its
 * assigned licensees is strictly forbidden by law.
 ******************************************************************************/

package org.mash.harness.db.sql;

import org.apache.log4j.Logger;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.db.DBResult;
import org.mash.harness.wait.PollingWaitHarness;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

import java.util.List;

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
 * @author teastlack
 * @since Sep 23, 2010 10:24:06 AM
 */
public class DBWaitHarness extends PollingWaitHarness
{
    private static final Logger log = Logger.getLogger(DBWaitHarness.class.getName());

    private String url;
    private String user;
    private String password;
    private String driver;
    private String sql;

    private Integer size = 1;
    private DBResult result;

    private SQLRunHarness run;

    @Override
    protected boolean poll(List<RunHarness> previous, List<SetupHarness> setups)
    {
        boolean result = false;

        run = buildHarness();
        run.run(previous, setups);
        DBResult response = run.getResponse();

        if (response != null)
        {
            int responseSize = response.getResultSetData().size();
            if (responseSize == size)
            {
                result = true;
            }
            else
            {
                log.info("Found " + responseSize + " records");
            }
        }
        return result;
    }

    @Override
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
