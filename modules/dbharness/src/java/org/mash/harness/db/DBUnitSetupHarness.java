package org.mash.harness.db;

import org.apache.log4j.Logger;
import org.mash.config.Configuration;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.SetupHarness;

import java.util.List;

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
 * <li>DELETE (only delete the rows defined by the XML)</li>
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
 *
 */
public class DBUnitSetupHarness extends BaseHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(DBUnitSetupHarness.class.getName());
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
    }

    public DBWorker getWorker() throws Exception
    {
        if (util == null)
        {
            util = (DBWorker) Class.forName(HARNESS_CONNECTOR).newInstance();
        }
        return util;
    }

    public void setConfiguration(List<Configuration> configs)
    {
        super.setConfiguration(configs);
        for (Configuration config : configs)
        {
            if ("url".equals(config.getName()))
            {
                url = config.getValue();
            }
            if ("user".equals(config.getName()))
            {
                user = config.getValue();
            }
            if ("password".equals(config.getName()))
            {
                password = config.getValue();
            }
            if ("driver".equals(config.getName()))
            {
                driver = config.getValue();
            }
            if ("type".equals(config.getName()) &&
                config.getValue() != null)
            {
                type = config.getValue().toUpperCase();
            }
        }
    }
}
