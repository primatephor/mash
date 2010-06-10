package org.mash.harness.db;

import org.apache.log4j.Logger;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.IOException;
import java.io.StringReader;

/**
 * The worker wraps the DBUnit interface, allowing for the flat file XML to be translated by DBUnit.
 * <p/>
 * User: teastlack Date: Jul 7, 2009 Time: 5:32:08 PM
 */
public class DBUnitWorker
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

    public void updateRows(DBConnector connector, String theOperation, String content) throws Exception
    {
        DBOperation operation = DBOperation.find(theOperation);
        IDatabaseConnection connection = getConnection(connector);
        try
        {
            log.info("Runing operation " + operation.name() + " on " + connector.toString());
            log.debug("Using content:\n" + content);
            operation.getOperation().execute(connection, getDataSet(content));
        }
        finally
        {
            connection.getConnection().close();
            connection.close();
        }
    }
}
