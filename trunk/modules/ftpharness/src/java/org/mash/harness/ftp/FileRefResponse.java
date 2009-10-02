package org.mash.harness.ftp;

import org.mash.harness.OGNLResponse;

import java.io.File;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 3:43:00 PM
 *
 */
public class FileRefResponse extends OGNLResponse
{
    private File theFile;

    public FileRefResponse(File file)
    {
        super(file);
        theFile = file;
    }

    public String getValue(String name)
    {
        String result;
        if ("length".equalsIgnoreCase(name))
        {
            result = String.valueOf(theFile.length());
        }
        else
        {
            result = super.getValue(name);
        }
        return result;
    }

    public File getTheFile()
    {
        return theFile;
    }
}
