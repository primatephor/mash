package org.mash.harness.ftp;

import org.mash.harness.RunResponse;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 2:35:30 PM
 *
 */
public class FileContentsResponse implements RunResponse
{
    private String fileContents;

    public FileContentsResponse(String fileContents)
    {
        this.fileContents = fileContents;
    }

    public String getValue(String name)
    {
        return getString();
    }

    public Collection<String> getValues(String name)
    {
        return getValues();
    }

    public Collection<String> getValues()
    {
        return Arrays.asList(getString());
    }

    public String getString()
    {
        return fileContents;
    }
}
