package org.mash.harness.db.sql;

import org.apache.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.db.DBConnector;
import org.mash.harness.db.DBResult;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
 * <li>sql (any sql to run)</li>
 * </ul>
 *
 * @author teastlack
 * @since Jan 8, 2010 3:22:30 PM
 */
@HarnessName(name = "sql")
public class SQLRunHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(SQLRunHarness.class.getName());

    private String url;
    private String user;
    private String password;
    private String schema;
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
        for (Parameter parameter : getParameters())
        {
            if (parameter.getFile() != null)
            {
                try
                {
                    log.info("Running sql file " + parameter.getFile());
                    runSql(connection, parameter.getValue());
                }
                catch (Exception e)
                {
                    log.error("Unexpected error executing db actions", e);
                    addError("Unexpected error executing db actions", e.getMessage());
                }
            }
        }
    }

    protected DBConnector getConnector()
    {
        if (schema == null)
        {
            return new DBConnector(url, user, password, driver);
        }
        else
        {
            return new DBConnector(url, user, password, driver, schema);
        }
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
            addError("Failed to execute sql '" + sql + "'", e);
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
        if (statement.execute(sql))
        {
            ResultSet results = statement.getResultSet();
            if (!results.next())
            {
                log.info("No results found for sql ");
            }
            else
            {
                log.debug("Setting results");
                result = new DBResult(results);
            }
            results.close();
        }
    }

    public DBResult getResponse()
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

    @HarnessConfiguration(name = "schema")
    public void setSchema(String schema)
    {
        this.schema = schema;
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
