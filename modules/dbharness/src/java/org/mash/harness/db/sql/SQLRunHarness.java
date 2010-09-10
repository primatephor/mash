package org.mash.harness.db.sql;

import org.mash.harness.RunHarness;
import org.mash.harness.BaseHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.HarnessError;
import org.mash.harness.db.DBResult;
import org.mash.harness.db.DBConnector;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;
import org.apache.log4j.Logger;

import java.util.List;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Run an sql statement.  String values should be quoted!
 * <p/>
 * <p/>
 * Necessary configurations are
 * <ul>
 * <li>url (the db url)</li>
 * <li>user (the db username)</li>
 * <li>password (the db password)</li>
 * <li>driver (the db driver)</li>
 * </ul>
 * <p/>
 * Parameters are:
 * <ul>
 * <li>sql (any sql to run) OR</li>
 * </ul>
 *
 * @author teastlack
 * @since Jan 8, 2010 3:22:30 PM
 */
public class SQLRunHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(SQLRunHarness.class.getName());

    private String url;
    private String user;
    private String password;
    private String driver;

    private String sql;

    private DBResult result;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        DBConnector connection = getConnector();
        if (sql != null)
        {
            runSql(connection, sql);
        }
        else
        {
            getErrors().add(new HarnessError(this, "Run SQL", "No sql supplied"));
        }

        if (result == null ||
            result.getResultSet(0) == null ||
            result.getResultSet(0).size() == 0)
        {
            getErrors().add(new HarnessError(this,
                                             "Retrieve Row",
                                             "No results found for sql '" + sql + "'"));
        }
    }

    protected DBConnector getConnector()
    {
        return new DBConnector(url, user, password, driver);
    }

    protected void runSql(DBConnector connector, String sql)
    {
        log.trace("Running sql:" + sql);
        Connection connection = null;
        Statement statement = null;
        try
        {
            connection = connector.getConnection();
            statement = connection.createStatement();
            processSql(sql, statement);
        }
        catch (Exception e)
        {
            log.error("Unexpected error", e);
            getErrors().add(new HarnessError(getName(), "Failed to execute sql '" + sql + "'", e));
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                }
            }
            catch (SQLException e)
            {
                log.error("Unexpected error closing statement/connection", e);
            }
        }
    }

    protected void processSql(String sql, Statement statement) throws Exception
    {
        ResultSet results = statement.executeQuery(sql);
        if (!results.next())
        {
            log.warn("No results found for sql " + sql);
        }
        else
        {
            log.debug("Setting results");
            result = new DBResult(results);
        }
        results.close();
    }

    public RunResponse getResponse()
    {
        return result;
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
}
