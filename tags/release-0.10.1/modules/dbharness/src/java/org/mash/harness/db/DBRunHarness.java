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
 * Parameters are:
 * <ul>
 * <li>sql (any sql to run) OR</li>
 * <li>table_name</li>
 * <li>column_name</li>
 * <li>column_value</li>
 * </ul>
 *
 * If no sql is specifed, there should be table, column, and value data.  This will generate the following sql:
 * 'select * from table_name where column_name=column_value'.  Keep in mind: if column_value is a string, surround
 * with ' (e.g. 'somevalue').
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
    private String sql;

    private DBResult result;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        DBConnector connection = new DBConnector();
        connection.setDriver(driver);
        connection.setUrl(url);
        connection.setUser(user);
        connection.setPassword(password);

        if (sql != null)
        {
            runSql(connection, sql);
        }
        else
        {
            retrieveRow(connection, tableName, columnName, columnValue);
        }
        if (result == null ||
            result.getResultSet(0) == null ||
            result.getResultSet(0).size() == 0)
        {
            getErrors().add(new HarnessError(this,
                                             "Retrieve Row",
                                             "No results found for table '" + tableName +
                                             "', column '" + columnName +
                                             "', value '" + columnValue + "'"));
        }
    }

    private void retrieveRow(DBConnector connection, String tableName, String columnName, String columnValue)
    {
        String sql = "select * from " + tableName + " where " + columnName + "=" + columnValue;
        runSql(connection, sql);
    }

    private void runSql(DBConnector connection, String sql)
    {
        try
        {
            JDBCWorker worker = getWorker();
            worker.execute(connection, sql);
            result = worker.getResult();
        }
        catch (Exception e)
        {
            log.error("Unexpected error", e);
            getErrors().add(new HarnessError(getName(), "Failed to execute sql '" + sql + "'", e));
        }
    }

    public JDBCWorker getWorker()
    {
        return new JDBCWorker();
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

    @HarnessParameter(name = "sql")
    public void setSql(String sql)
    {
        this.sql = sql;
    }
}
