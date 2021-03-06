package org.mash.harness;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *  Date: Jul 7, 2009 Time: 10:59:24 AM
 */
public class HarnessError
{
    private static final Logger log = LogManager.getLogger(HarnessError.class.getName());
    private String harnessName;
    private String value;
    private String description;

    public HarnessError(Harness harness, String value, String description)
    {
        this(harness.getName(), value, description);
    }

    public HarnessError(Harness harness, String value, Throwable e)
    {
        this(harness.getName(), value, e);
    }

    public HarnessError(String harnessName, String value, String description)
    {
        this.harnessName = harnessName;
        this.value = value;
        this.description = description;
    }

    public HarnessError(String harnessName, String value, Throwable e)
    {
        this.harnessName = harnessName;
        this.value = value;
        this.description = e.getClass().getName() + ":" + e.getMessage();
        log.error("Error exception", e);
        if (e.getMessage() != null && e.getMessage().length() == 0 && e.getCause() != null)
        {
            this.description = e.getCause().getMessage();
        }
    }

    public String getHarnessName()
    {
        return harnessName;
    }

    public String getValue()
    {
        return value;
    }

    public String getDescription()
    {
        return description;
    }
}
