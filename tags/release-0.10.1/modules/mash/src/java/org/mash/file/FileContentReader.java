package org.mash.file;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Retrieve a file using a uri or java File object. Attempts first to retrieve the file url using the classloader, then
 * will attempt to open the file directly using the submitted url if that doesn't work.
 *
 * @author teastlack
 * @since Sep 22, 2009 3:05:39 PM
 *
 */
public abstract class FileContentReader
{
    private static final Logger log = Logger.getLogger(TextFileReader.class.getName());
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

    public abstract String getContents(InputStream stream) throws FileReaderException;
}
