
package org.mash.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * @author
 * @since Mar 22, 2011 9:40:46 AM
 */
public class FileReloadTimer
{
    private static final Logger log = LogManager.getLogger(FileReloadTimer.class.getName());
    private URL url;
    private Date timeToCheck;
    private Date lastLoaded;
    //default to check only every 30 seconds
    private long waitToCheckLoadTime = 30000l;

    /**
     * @param url to manage load time for
     */
    public FileReloadTimer(URL url)
    {
        this.url = url;
    }

    public void reloaded()
    {
        lastLoaded = new Date();
    }

    public Date getLastLoaded()
    {
        return lastLoaded;
    }

    public void forceReload()
    {
        timeToCheck = null;
        lastLoaded = null;
    }

    public long getWaitToCheckLoadTime()
    {
        return waitToCheckLoadTime;
    }

    public void setWaitToCheckLoadTime(long waitToCheckLoadTime)
    {
        this.waitToCheckLoadTime = waitToCheckLoadTime;
        timeToCheck = new Date(lastLoaded.getTime() + this.waitToCheckLoadTime);
    }

    /**
     * Determine if a file should be reloaded.  This happens by initializing a load time at construction, and comparing
     * the file's lastModified time to that last load.  If the file has been modified since the last load, it will be
     * reloaded.
     *
     * This is only checked when enough time has lapsed (specifed by the waitToCheckLoadTime, default = 30s)
     *
     * @return true if reload should happen
     */
    public Boolean shouldReload()
    {
        Boolean result = false;
        if (url != null)
        {
            Date now = new Date();
            if (timeToCheck == null ||
                timeToCheck.getTime() < now.getTime())
            {
                try
                {
                    String path = url.toURI().getPath();
                    File propertyFile = new File(path);
                    if (propertyFile.exists())
                    {
                        long lastload = 0l;
                        if (lastLoaded != null)
                        {
                            lastload = lastLoaded.getTime();
                        }
                        log.debug(hashCode() + "-Checking " + propertyFile.getAbsolutePath() +
                                  " last modified:" + propertyFile.lastModified() +
                                  " last loaded:" + lastload);
                        if (propertyFile.lastModified() > lastload)
                        {
                            log.info(hashCode() + "-Will reload " + propertyFile.getAbsolutePath() +
                                     " last modified:" + propertyFile.lastModified() +
                                     " last loaded:" + lastload);
                            result = true;
                        }
                    }
                    else
                    {
                        log.info("Cannot find file for reload:" + path);
                    }
                }
                catch (Exception e)
                {
                    log.error("Unexpected error retrieving file " + url.getFile(), e);
                }
                timeToCheck = new Date(now.getTime() + waitToCheckLoadTime);
            }
        }
        else
        {
            log.error("URL is not set!  Unable to provide load information!");
        }
        return result;
    }

    public URL getUrl()
    {
        return url;
    }

}
