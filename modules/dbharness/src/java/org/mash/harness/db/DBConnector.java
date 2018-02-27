package org.mash.harness.db;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 *  Date: Jul 7, 2009 Time: 6:35:29 PM
 */
public class DBConnector
{
    private static final Logger log = LogManager.getLogger(DBConnector.class.getName());
    private String url;
    private String user;
    private String password;
    private String schema;
    private String driver;

    public DBConnector(String url,
                       String user,
                       String password,
                       String driver,
                       String schema)
    {
        this(url, user, password, driver);
        this.schema = schema;
    }

    public DBConnector(String url,
                       String user,
                       String password,
                       String driver)
    {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUser()
    {
        return user;
    }

    public String getPassword()
    {
        return password;
    }

    public String getDriver()
    {
        return driver;
    }

    public String getSchema()
    {
        return schema;
    }

    public Connection getConnection() throws Exception
    {
        Class jdbcDriverClass = Class.forName(driver);
        Driver driverInstance = (Driver) jdbcDriverClass.newInstance();
        DriverManager.registerDriver(driverInstance);
        log.trace("Connecting to db:"+toString());
        return DriverManager.getConnection(url, user, password);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("URL:").append(url);
        buffer.append(", Driver:").append(driver);
        buffer.append(", User:").append(user);
        buffer.append(", Pass:").append(password);
        buffer.append(", Schema:").append(schema);
        return buffer.toString();
    }
}
