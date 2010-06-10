package org.mash.harness.db;

import org.apache.log4j.Logger;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 * @author teastlack
 * @since Jun 9, 2010 5:00:00 PM
 */
public class JDBCWorker
{
    private static final Logger log = Logger.getLogger(JDBCWorker.class.getName());
    private DBResult result;

    public void execute(DBConnector connector, String sql) throws Exception
    {
        Connection connection = null;
        Statement statement = null;
        try
        {
            log.trace("Running sql:" + sql);
            connection = getConnection(connector.getDriver(),
                                       connector.getUrl(),
                                       connector.getUser(),
                                       connector.getPassword());
            statement = connection.createStatement();
            processSql(sql, statement);
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

    public DBResult getResult()
    {
        return result;
    }

    public Connection getConnection(String driver,
                                    String url,
                                    String user,
                                    String password) throws Exception
    {
        Class jdbcDriverClass = Class.forName(driver);
        Driver driverInstance = (Driver) jdbcDriverClass.newInstance();
        DriverManager.registerDriver(driverInstance);
        return DriverManager.getConnection(url, user, password);
    }
}
