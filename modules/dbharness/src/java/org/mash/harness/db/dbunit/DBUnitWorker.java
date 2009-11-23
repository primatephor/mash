package org.mash.harness.db.dbunit;

import org.apache.log4j.Logger;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.mash.harness.db.DBConnector;
import org.mash.harness.db.DBWorker;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Statement;

/**
 * The worker wraps the DBUnit interface, allowing for the flat file XML to be translated by DBUnit.
 *
 * User: teastlack Date: Jul 7, 2009 Time: 5:32:08 PM
 */
public class DBUnitWorker implements DBWorker
{
    private static final Logger log = Logger.getLogger(DBUnitWorker.class.getName());
    private IDatabaseTester databaseTester;

    public IDatabaseConnection getConnection(DBConnector connector) throws Exception
    {
        if (this.databaseTester == null)
        {
            log.info("Instantiating DB info [driver='" + connector.getDriver() +
                     "', url='" + connector.getUrl() +
                     "', user='" + connector.getUser() +
                     "', pass='" + connector.getPassword() + "']");
            this.databaseTester = new JdbcDatabaseTester(connector.getDriver(),
                                                         connector.getUrl(),
                                                         connector.getUser(),
                                                         connector.getPassword());
        }
        return this.databaseTester.getConnection();
    }

    protected IDataSet getDataSet(String contents) throws IOException,
                                                          DataSetException
    {
        return new FlatXmlDataSet(new StringReader(contents));
    }

    public void execute(DBConnector connector, String sql) throws Exception
    {
        Connection conn = null;
        Statement stmt = null;
        if (sql != null && sql.trim().length() > 0)
        {
            try
            {
                conn = getConnection(connector).getConnection();
                stmt = conn.createStatement();
                log.debug("Exectuting " + sql);
                stmt.execute(sql);
            }
            finally
            {
                if (stmt != null)
                {
                    stmt.close();
                }
                if (conn != null)
                {
                    conn.close();
                }
            }
        }
    }

    public void updateRows(DBConnector connector, String theOperation, String content) throws Exception
    {
        DBOperation operation = DBOperation.find(theOperation);
        IDatabaseConnection connection = getConnection(connector);
        try
        {
            log.info("Runing operation " + operation.name());
            operation.getOperation().execute(connection, getDataSet(content));
        }
        finally
        {
            connection.close();
        }
    }
}
