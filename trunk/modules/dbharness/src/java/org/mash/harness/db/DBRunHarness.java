package org.mash.harness.db;

import org.mash.harness.RunHarness;
import org.mash.harness.BaseHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.HarnessError;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;
import org.apache.log4j.Logger;

import java.util.List;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Retrieve a row from a table, given the column name and value.  String values should be quoted! 
 * <p/>
 * Necessary configurations are
 * <ul>
 * <li>url (the db url)</li>
 * <li>user (the db username)</li>
 * <li>password (the db password)</li>
 * <li>driver (the db driver)</li>
 * </ul>
 *
 * @author teastlack
 * @since Jan 8, 2010 3:22:30 PM
 *
 */
public class DBRunHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(DBRunHarness.class.getName());

    private String url;
    private String user;
    private String password;
    private String driver;

    private String tableName;
    private String columnName;
    private String columnValue;

    private DBResult result;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        Connection connection = getConnection(driver, url, user, password);

        if (connection != null)
        {
            retrieveRow(connection, tableName, columnName, columnValue);
            if(result.getResultSetData().size() == 0)
            {
                getErrors().add(new HarnessError(getName(), "No results found for table '" + tableName +
                                                            "', column '" + columnName +
                                                            "', value '" + columnValue + "'"));
            }
        }
        else
        {
            getErrors().add(new HarnessError(getName(), "No connection established"));
        }
    }

    private void retrieveRow(Connection connection, String tableName, String columnName, String columnValue)
    {
        String sql = "select * from " + tableName + " where " + columnName + "=" + columnValue;
        Statement statement = null;
        try
        {
            log.info("Running sql:" + sql);
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            if(!results.next())
            {
                log.warn("No results found!");
            }
            result = new DBResult(results);
            results.close();
        }
        catch (SQLException e)
        {
            getErrors().add(new HarnessError(getName(), "Problem invoking sql", e));
        }
        finally
        {
            try
            {
                if(statement != null)
                {
                    statement.close();
                }
                if(connection != null)
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

    public RunResponse getResponse()
    {
        return result;
    }

    public Connection getConnection(String driver,
                                    String url,
                                    String user,
                                    String password)
    {
        Connection connection = null;
        Driver driverInstance;
        try
        {
            Class jdbcDriverClass = Class.forName(driver);
            driverInstance = (Driver) jdbcDriverClass.newInstance();
            DriverManager.registerDriver(driverInstance);
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (Exception e)
        {
            log.error("Failed to initialise driver " + e, e);
            getErrors().add(new HarnessError(getName(), "Failed to initialise driver " + driver, e));
        }
        return connection;
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

    @HarnessParameter(name = "table_name")
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    @HarnessParameter(name = "column_name")
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    @HarnessParameter(name = "column_value")
    public void setColumnValue(String columnValue)
    {
        this.columnValue = columnValue;
    }

}
