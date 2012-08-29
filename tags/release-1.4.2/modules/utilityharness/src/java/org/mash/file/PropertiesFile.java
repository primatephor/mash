package org.mash.file;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Observable;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author teastlack
 * @since Mar 22, 2011 9:39:49 AM
 */
public class PropertiesFile extends Observable
{
    private static final Logger log = Logger.getLogger(PropertiesFile.class.getName());

    private URL url;
    private Properties properties;
    private String propertiesFileName;
    private FileReloadTimer fileLoadTimer;
    private Lock lock = new ReentrantLock();

    public PropertiesFile(String propertiesFileName)
    {
        this.propertiesFileName = propertiesFileName;
        File file = getFile(propertiesFileName);
        URI uri = file.toURI();
        if (uri != null)
        {
            try
            {
                url = uri.toURL();
            }
            catch (MalformedURLException e)
            {
                log.error("Unexpected error converting " + propertiesFileName + " to url", e);
            }
        }
        initialize(url);
    }

    public PropertiesFile(File propertiesFile)
    {
        this.propertiesFileName = propertiesFile.getName();
        URI uri = propertiesFile.toURI();
        if (uri != null)
        {
            try
            {
                url = uri.toURL();
            }
            catch (MalformedURLException e)
            {
                log.error("Unexpected error retrieving file url" + propertiesFileName, e);
            }
        }
        initialize(url);
    }

    protected void initialize(URL url)
    {
        try
        {
            if (fileLoadTimer == null)
            {
                fileLoadTimer = new FileReloadTimer(url);
            }
            log.debug("URL to plugin resource file: " + url);
            properties = new java.util.Properties();
            properties.load(url.openStream());
            fileLoadTimer.reloaded();
            log.info(hashCode() +
                    "-Successfully loaded the properties file " + url.getPath() +
                    ", load time:" + fileLoadTimer.getLastLoaded().getTime());
        }
        catch (Exception ex)
        {
            log.warn("Unable to load the properties file " + url.getPath(), ex);
        }
    }

    public String getProperty(String propertyName)
    {
        return getProperty(propertyName, null);
    }

    /**
     * Retrieve properties from the properties object or file system
     *
     * @param propertyName key of property
     * @param defaultValue if no value is found, this is for default
     * @return String value of the property
     */
    public String getProperty(String propertyName,
                              String defaultValue)
    {
        if (fileLoadTimer.shouldReload())
        {
            if (url != null)
            {
                try
                {
                    lock.lock();
                    initialize(url);
                    super.setChanged();
                    super.notifyObservers(propertyName);
                }
                catch (Exception e)
                {
                    log.error("Unexpected error retrieving file " + url.getFile(), e);
                }
                finally
                {
                    lock.unlock();
                }
            }
            else
            {
                log.error("PROBLEM (URL null) reading properties file:" + propertiesFileName);
            }
        }

        String result = null;
        if (properties != null)
        {
            result = properties.getProperty(propertyName, defaultValue);
            log.trace("Property:" + propertyName + "=" + result);
        }
        return result;
    }

    public void setProperty(String key,
                            String value) throws Exception
    {
        setProperty(key, value, "---No Comment---");
    }

    /**
     * Set and write the property to the filesystem
     *
     * @param key name of property
     * @param value to set
     * @param comment any comment
     * @throws Exception (probably around writing to fs)
     */
    public void setProperty(String key,
                            String value,
                            String comment) throws Exception
    {
        if (key != null)
        {
            properties.setProperty(key, value);
            try
            {
                lock.lock();
                log.debug("Writing properties to " + url.getFile());
                String propName = URLDecoder.decode(url.getFile(), "UTF-8");
                FileWriter out = new FileWriter(propName);
                properties.store(out, comment);
                out.flush();
                out.close();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Unable to update the plugin properties.", e);
            }
            finally
            {
                lock.unlock();
            }
        }
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void forceReload()
    {
        fileLoadTimer.forceReload();
    }

    public String getPropertiesFileName()
    {
        return propertiesFileName;
    }

    public long getWaitToCheckLoadTime()
    {
        return fileLoadTimer.getWaitToCheckLoadTime();
    }

    public void setWaitToCheckLoadTime(long waitToCheckLoadTime)
    {
        fileLoadTimer.setWaitToCheckLoadTime(waitToCheckLoadTime);
    }

    public File getFile(String fileName)
    {
        log.trace("Looking for file " + fileName);
        URL url = this.getClass().getClassLoader().getResource(fileName);
        if (url == null)
        {
            url = ClassLoader.getSystemResource(fileName);
        }

        File result = null;
        if (url != null)
        {
            try
            {
                result = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
            }
            catch (Exception e)
            {
                log.error("Unexpected error creating file from url" + e.getMessage());
            }
        }
        if (result == null)
        {
            result = new File(fileName);
        }
        return result;
    }
}
