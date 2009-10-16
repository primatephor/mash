package org.mash.harness;

/**
 * User: teastlack Date: Jul 7, 2009 Time: 10:59:24 AM
 */
public class HarnessError
{
    private String harnessName;
    private String value;
    private String description;

    public HarnessError(String harnessName, String value, String description)
    {
        this.harnessName = harnessName;
        this.value = value;
        this.description = description;
    }

    public HarnessError(String harnessName, String value)
    {
        this.harnessName = harnessName;
        this.value = value;
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