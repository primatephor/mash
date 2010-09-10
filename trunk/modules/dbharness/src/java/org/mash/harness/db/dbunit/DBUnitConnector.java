package org.mash.harness.db.dbunit;

import org.mash.harness.db.DBConnector;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.apache.log4j.Logger;

/**
 * @author: teastlack
 * @since: Sep 9, 2010
 */
public class DBUnitConnector extends DBConnector
{
    private static final Logger log = Logger.getLogger(DBUnitConnector.class.getName());
    private IDatabaseTester databaseTester;

    public DBUnitConnector(String url, String user, String password, String driver)
    {
        super(url, user, password, driver);
    }

    public IDatabaseConnection getDBUnitConnection() throws Exception
    {
        if (this.databaseTester == null)
        {
            log.info("Instantiating DB info [driver='" + getDriver() +
                     "', url='" + getUrl() +
                     "', user='" + getUser() +
                     "', pass='" + getPassword() + "']");
            this.databaseTester = new JdbcDatabaseTester(getDriver(),
                                                         getUrl(),
                                                         getUser(),
                                                         getPassword());
        }
        return this.databaseTester.getConnection();
    }
}
