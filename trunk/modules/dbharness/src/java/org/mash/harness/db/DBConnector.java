package org.mash.harness.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 * User: teastlack Date: Jul 7, 2009 Time: 6:35:29 PM
 */
public class DBConnector
{
    private String url;
    private String user;
    private String password;
    private String driver;

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

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getDriver()
    {
        return driver;
    }

    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    public Connection getConnection() throws Exception
    {
        Class jdbcDriverClass = Class.forName(driver);
        Driver driverInstance = (Driver) jdbcDriverClass.newInstance();
        DriverManager.registerDriver(driverInstance);
        return DriverManager.getConnection(url, user, password);
    }


    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("URL:").append(url);
        buffer.append(", Driver:").append(driver);
        buffer.append(", User:").append(user);
        buffer.append(", Pass:").append(password);
        return buffer.toString();
    }
}
