package org.mash.loader;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Read from the filesystem to load tests.  Attempts first to retrieve the file url using the classloader, then wil
 * attempt to open the file directly using the submitted url if that doesn't work.
 * <p/>
 * The entire file is read into memory, so play nice.
 * <p/>
 * User: teastlack Date: Jul 1, 2009 Time: 10:56:54 AM
 */
public class TextFileReader
{
    private static final Logger log = Logger.getLogger(TextFileReader.class.getName());
    public static String separator = System.getProperty("line.separator");
    private FileLoader loader = new FileLoader();

    /**
     * Retrieve the contents of the file given the URI
     *
     * @param fileUri to retrieve
     * @return string contents of the file
     * @throws FileReaderException when there's a problem retrieving the file
     */
    public String getContents(String fileUri) throws
                                              FileReaderException
    {
        String contents = null;
        if (!"".equals(fileUri))
        {
            try
            {
                InputStream stream = loader.findStream(fileUri);
                if (stream != null)
                {
                    contents = getContents(stream);
                }
                else
                {
                    File basedir = new File(".");
                    throw new FileReaderException("FILE NOT FOUND:" + fileUri + " in directory:" + basedir.getAbsolutePath());
                }
            }
            catch (Exception e)
            {
                throw new FileReaderException("Unexpected error retrieving " + fileUri, e);
            }
        }
        return contents;
    }

    /**
     * Retrieve the contents of the specified file object
     *
     * @param file to retrieve
     * @return string contents of the file
     * @throws FileReaderException when something goes wrong
     */
    public String getContents(File file) throws
                                         FileReaderException
    {
        String contents = null;
        if (!"".equals(file.getAbsolutePath()))
        {
            try
            {
                FileInputStream inputStream = new FileInputStream(file);
                contents = getContents(inputStream);
            }
            catch (Exception e)
            {
                throw new FileReaderException("Problem retrieving stream from File object", e);
            }
        }
        else
        {
            log.error("File does not contain any path");
        }
        return contents;
    }

    /**
     * Retrieve the contents of the specified stream
     *
     * @param stream to retrieve
     * @return string contents of the file
     * @throws FileReaderException when something goes wrong
     */
    public String getContents(InputStream stream) throws FileReaderException
    {
        StringBuilder contents = new StringBuilder();
        BufferedReader input = null;
        try
        {
            if (stream != null)
            {
                input = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = input.readLine()) != null)
                {
                    contents.append(line).append(separator);
                }
            }
        }
        catch (IOException e)
        {
            throw new FileReaderException("Problem reading from file stream", e);
        }
        finally
        {
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                    //yes, this could mask exceptions above
                    throw new FileReaderException("Problem closing file", e);
                }
            }
        }
        return contents.toString();
    }
}
