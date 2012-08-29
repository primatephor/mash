package org.mash.harness.selenium;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;
import org.openqa.selenium.server.cli.RemoteControlLauncher;
import org.openqa.selenium.server.htmlrunner.HTMLLauncher;

import java.io.File;

/**
 * todo: hook into setup and teardown
 * todo: make configuration appropriate to server for startup
 * todo: file loading per mash
 *
 * @author teastlack
 * @since 12/13/11 5:00 PM
 */
public class NonExitingSeleniumServer extends SeleniumServer
{
    protected File in;
    protected File out;

    private String browser;
    private String startURL;

    /**
     * Constructs a non exiting selenium server.
     *
     * @param in  the selenium test suite
     * @param out the selenium report
     * @param browser to use
     * @throws Exception if the server can not be created
     */
    public NonExitingSeleniumServer(File in, File out, String browser) throws Exception
    {
        super(getConfig());
        this.in = in;
        this.out = out;
        this.browser = browser;
    }

    static RemoteControlConfiguration getConfig()
    {
        RemoteControlConfiguration config = new RemoteControlConfiguration();
        config.setHTMLSuite(true);
        config.setDebugMode(true);
        config.setTimeoutInSeconds(10);
        config.setProfilesLocation(new File(System.getProperty("java.io.tmpdir")));
        return config;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.openqa.selenium.server.SeleniumServer#runHtmlSuite()
     */
    @Override
    protected void runHtmlSuite()
    {
        final String result;
        try
        {
            if (!in.exists())
            {
                RemoteControlLauncher.usage("Can't find HTML Suite file:" + in.getAbsolutePath());
            }
            addNewStaticContent(in.getParentFile());
            HTMLLauncher launcher = new HTMLLauncher(this);
            out.createNewFile();

            if (!out.canWrite())
            {
                RemoteControlLauncher.usage("can't write to result file " + out.getAbsolutePath());
            }

            result = launcher.runHTMLSuite(browser, startURL, in, out, 100, false);

            if (!"PASSED".equals(result))
            {
                System.err.println("Tests failed, see result file for details: " + out.getAbsolutePath());
            }
        }
        catch (Exception e)
        {
            System.err.println("HTML suite exception seen:");
            e.printStackTrace();
        }
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getStartURL()
    {
        return startURL;
    }

    public void setStartURL(String startURL)
    {
        this.startURL = startURL;
    }
}
