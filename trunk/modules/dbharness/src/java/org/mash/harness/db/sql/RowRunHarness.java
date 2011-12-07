package org.mash.harness.db.sql;

import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.db.DBConnector;
import org.mash.harness.db.DBResult;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

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
 * <p/>
 * Parameters are:
 * <ul>
 * <li>table_name</li>
 * <li>column_name</li>
 * <li>column_value</li>
 * </ul>
 * <p/>
 * There should be table, column, and column value data.  This will generate the following sql:
 * 'select * from table_name where column_name=column_value'.
 * Keep in mind: if column_value is a string, surround with ' (e.g. 'somevalue').
 *
 * @author teastlack
 * @since Sep 9, 2010
 */
@HarnessName(name = "db_row")
public class RowRunHarness extends SQLRunHarness implements RunHarness
{
    private String tableName;
    private String columnName;
    private String columnValue;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        DBConnector connection = getConnector();

        retrieveRow(connection, tableName, columnName, columnValue);
        DBResult result = getResponse();
        if (result == null ||
            result.getResultSet(0) == null ||
            result.getResultSet(0).size() == 0)
        {
            addError("Retrieve Row",
                     "No results found for table '" + tableName +
                     "', column '" + columnName +
                     "', value '" + columnValue + "'");
        }
    }

    private void retrieveRow(DBConnector connection, String tableName, String columnName, String columnValue)
    {
        String sql = "select * from " + tableName + " where " + columnName + "=" + columnValue;
        runSql(connection, sql);
    }

    @HarnessConfiguration(name = "url")
    public void setUrl(String url)
    {
        super.setUrl(url);
    }

    @HarnessConfiguration(name = "user")
    public void setUser(String user)
    {
        super.setUser(user);
    }

    @HarnessConfiguration(name = "password")
    public void setPassword(String password)
    {
        super.setPassword(password);
    }

    @HarnessConfiguration(name = "driver")
    public void setDriver(String driver)
    {
        super.setDriver(driver);
    }

    @HarnessConfiguration(name = "schema")
    public void setSchema(String schema)
    {
        super.setSchema(schema);
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
