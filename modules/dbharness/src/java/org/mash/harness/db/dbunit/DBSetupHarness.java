package org.mash.harness.db.dbunit;

import org.apache.log4j.Logger;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessConfiguration;

import java.io.IOException;
import java.io.StringReader;

/**
 * Files run are DBUnit type flat xml data sets.
 * <p/>
 * Necessary configurations are
 * <ul>
 * <li>url (the db url)</li>
 * <li>user (the db username)</li>
 * <li>password (the db password)</li>
 * <li>driver (the db driver)</li>
 * <li>type (the type of db operation, see below)</li>
 * </ul>
 * <p/>
 * Valid types (default is INSERT):
 * <ul>
 * <li>CLEAN_INSERT (delete and re insert the rows defined by the XML)</li>
 * <li>DELETE (only delete the rows defined by the XML)  WATCH OUT! This won't take XML and delete everything!</li>
 * <li>DELETE_ALL (delete all rows defined by tables in XML)</li>
 * <li>INSERT (Insert data defined in the xml)</li>
 * <li>REFRESH (insert new rows in db, update existing rows defined in XML)</li>
 * <li>TRUNCATE (truncate tables)</li>
 * <li>UPDATE (perform update on rows defined by xml)</li>
 * </ul>
 * <p/>
 * Each file listed in the parameters (or content submitted) is run against the db.
 * <p/>
 * <p/>
 * Be mindful when combining multiple flat XML sets with the types of updates to perform.  A 'CLEAN_INSERT' will clean
 * the db with each load, and may wipe out previously loaded tables.
 * <p/>
 *
 * @author teastlack
 * @since Jul 7, 2009 5:20:41 PM
 */
public class DBSetupHarness extends BaseHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(DBSetupHarness.class.getName());

    private String url;
    private String user;
    private String password;
    private String driver;
    private String type;

    public void setup() throws Exception
    {
        DBUnitConnector connector = getConnector();
        for (Parameter parameter : getParameters())
        {
            try
            {
                if (parameter.getFile() != null)
                {
                    log.info("Running db setup file " + parameter.getFile());
                    updateRows(connector, type, parameter.getValue());
                }
            }
            catch (Exception e)
            {
                log.error("Unexpected error executing db actions", e);
                addError("Unexpected error executing db actions", e.getMessage());
            }
        }
    }

    protected DBUnitConnector getConnector()
    {
        return new DBUnitConnector(url, user, password, driver);
    }

    protected IDataSet getDataSet(String contents) throws IOException,
                                                          DataSetException
    {
        return new FlatXmlDataSet(new StringReader(contents));
    }

    public void updateRows(DBUnitConnector connector, String theOperation, String content) throws Exception
    {
        DBOperation operation = DBOperation.find(theOperation);
        IDatabaseConnection connection = connector.getDBUnitConnection();
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

    @HarnessConfiguration(name = "type")
    public void setType(String type)
    {
        if (type != null)
        {
            this.type = type.toUpperCase();
        }
    }
}
