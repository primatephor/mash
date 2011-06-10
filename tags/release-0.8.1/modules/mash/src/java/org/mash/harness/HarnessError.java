package org.mash.harness;

/**
 * User: teastlack Date: Jul 7, 2009 Time: 10:59:24 AM
 */
public class HarnessError
{
    private String harnessName;
    private String value;
    private String description;

    public HarnessError(BaseHarness harness, String value, String description)
    {
        this(harness.getName(), value, description);
    }

    public HarnessError(BaseHarness harness, String value, Exception e)
    {
        this(harness.getName(), value, e);
    }

    public HarnessError(String harnessName, String value, String description)
    {
        this.harnessName = harnessName;
        this.value = value;
        this.description = description;
    }

    public HarnessError(String harnessName, String value, Exception e)
    {
        this.harnessName = harnessName;
        this.value = value;
        this.description = e.getMessage();
        if ((this.description == null ||
             this.description.trim().length() == 0) && e.getCause() != null)
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