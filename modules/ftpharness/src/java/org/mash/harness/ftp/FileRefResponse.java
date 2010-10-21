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

    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        if(this.theFile != null)
        {
            buffer.append("File:").append(theFile.getAbsolutePath()).append("\n");
            buffer.append("Size:").append(theFile.getTotalSpace()).append("\n");
            buffer.append("Read:").append(theFile.canRead()).append("\n");
            buffer.append("Write").append(theFile.canWrite()).append("\n");
        }
        return buffer.toString();
    }
}
