package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.mash.file.FileLoader;
import org.mash.file.FileReaderException;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 5:23:59 PM
 *
 */
public class BogusFTPClient extends FTPClient
{
    private String path;
    private FileLoader loader = new FileLoader();

    public InputStream retrieveFileStream(String s) throws IOException
    {
        InputStream stream;

        try
        {
            stream = loader.findStream(path);
        }
        catch (FileReaderException e)
        {
            throw new IOException(e);
        }

        return stream;
    }

    public boolean completePendingCommand() throws IOException
    {
        return true;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }
}
