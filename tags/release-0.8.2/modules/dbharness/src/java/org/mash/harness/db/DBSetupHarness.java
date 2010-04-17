package org.mash.harness.db;

import org.apache.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessConfiguration;

/**
 * Files run may be either DBUnit type flat xml data sets or raw sql.
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
 * Valid types (when file is .xml, default is INSERT):
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
 * Each file listed in the parameters (or content submitted) is run against the db.  Only SQL is valid content specified
 * within a test.
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
    private static String HARNESS_CONNECTOR = System.getProperty("db.setup.class", "org.mash.harness.db.dbunit.DBUnitWorker");
    private DBWorker util;
    private String url;
    private String user;
    private String password;
    private String driver;
    private String type;

    public void setup() throws Exception
    {
        DBWorker util = getWorker();
        DBConnector connector = new DBConnector();
        connector.setDriver(driver);
        connector.setPassword(password);
        connector.setUrl(url);
        connector.setUser(user);

        for (Parameter parameter : getParameters())
        {
            try
            {
                if (parameter.getFile() != null)
                {
                    log.info("Running db setup file " + parameter.getFile());
                    if (parameter.getFile().endsWith(".sql"))
                    {
                        util.execute(connector, parameter.getValue());
                    }
                    else if (parameter.getFile().endsWith(".xml"))
                    {
                        util.updateRows(connector, type, parameter.getValue());
                    }
                }
            }
            catch (Exception e)
            {
                log.error("Unexpected error executing db actions", e);
                getErrors().add(new HarnessError(this.getName(), "Unexpected error executing db actions", e.getMessage()));
            }
        }
    }

    public DBWorker getWorker() throws Exception
    {
        if (util == null)
        {
            util = (DBWorker) Class.forName(HARNESS_CONNECTOR).newInstance();
        }
        return util;
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
