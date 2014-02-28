package org.mash.file;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Finds a file on the filesystem, returning a File object or Stream
 *
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class FileLoader
{
    private static final Logger log = Logger.getLogger(FileLoader.class.getName());

    public File findFile(String filename, File base) throws FileReaderException
    {
        log.info("Finding file for load:" + filename);
        File result = null;
        if (filename != null)
        {
            File check = createFile(filename);
            if (check.exists())
            {
                log.debug("Using test path:" + filename);
                result = check;
            }
            else if (base != null)
            {
                String basePath = base.getPath();
                File baseFile = createFile(basePath);
                log.debug("Looking for file in base " + basePath);
                if (baseFile != null && baseFile.exists())
                {
                    basePath = baseFile.getParent();
                }

                basePath = normalizePath(basePath);
                String relativePath = basePath + filename;
                log.debug("Looking for file with base path:" + relativePath);
                check = createFile(relativePath);
                if (check.exists())
                {
                    log.debug("Using base path:" + relativePath);
                    result = check;
                }
            }

        }
        if (result == null)
        {
            log.warn("Unable to find file with name " + filename);
            throw new FileReaderException("Unable to find file with name " + filename);
        }
        return result;
    }

    private String normalizePath(String path)
    {
        if (!path.endsWith("/"))
        {
            path = path + "/";
        }
        return path;
    }

    public File createFile(String filename)
    {
        File result = new File(filename);
        URL url = getClass().getClassLoader().getResource(filename);
        if (url != null)
        {
            String theName = url.getFile();
            if (theName != null)
            {
                result = new File(theName);
            }
        }
        return result;
    }

    public InputStream findStream(String filename) throws FileReaderException
    {
        return findStream(filename, null);
    }

    public InputStream findStream(String filename, File base) throws FileReaderException
    {
        InputStream stream;
        try
        {
            URL url = getClass().getClassLoader().getResource(filename);
            if (url == null && base != null)
            {
                log.debug("Loading Stream from URL " + base.getPath() + "/" + filename);
                url = getClass().getClassLoader().getResource(base.getPath() + "/" + filename);
            }
            if (url != null)
            {
                stream = url.openStream();
            }
            else
            {
                File basedir = findFile(filename, base);
                if (basedir != null)
                {
                    log.debug("Loading Stream from file " + basedir.getAbsolutePath());
                    stream = new FileInputStream(basedir);
                }
                else
                {
                    String baseString = null;
                    if (base != null)
                    {
                        baseString = base.getAbsolutePath();
                    }
                    throw new FileReaderException("Unable to find file " + filename + " using base '" + baseString + "'");
                }
            }

        }
        catch (Exception e)
        {
            throw new FileReaderException("Unexpected error getting filestream for " + filename, e);
        }
        return stream;
    }
}
