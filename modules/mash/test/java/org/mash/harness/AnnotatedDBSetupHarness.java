package org.mash.harness;

import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

/**
 *
 * @author
 * @since Sep 17, 2009 1:44:33 PM
 *
 */
public class AnnotatedDBSetupHarness extends BaseHarness implements SetupHarness
{
    public Boolean setupCalled = false;
    private String type;
    private String url;
    private String user;
    private String pass;
    private String driver;
    private String load;

    public void setup()
    {
        setupCalled = true;
    }

    public String getType()
    {
        return type;
    }

    @HarnessConfiguration(name = "type")
    public void setType(String type)
    {
        this.type = type;
    }

    public String getUrl()
    {
        return url;
    }

    @HarnessConfiguration(name = "url")
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUser()
    {
        return user;
    }

    @HarnessConfiguration(name = "user")
    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPass()
    {
        return pass;
    }

    public void setPass(String pass)
    {
        this.pass = pass;
    }

    public String getDriver()
    {
        return driver;
    }

    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    public String getLoad()
    {
        return load;
    }

    @HarnessParameter(name = "loadfile")
    public void setLoad(String load)
    {
        this.load = load;
    }
}
