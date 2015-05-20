package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPFile;
import org.mash.harness.RunResponse;
import org.mash.tool.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author
 * @since Sep 22, 2009 11:39:12 AM
 *
 */
public class ListRunResponse implements RunResponse
{
    private Map<String, FTPFile> files;

    public ListRunResponse(FTPFile[] files)
    {
        if (files != null)
        {
            for (FTPFile file : files)
            {
                this.getFiles().put(file.getName(), file);
            }
        }
    }

    public String getValue(String name)
    {
        String result = null;
        if (this.getFiles().get(name) != null)
        {
            result = this.getFiles().get(name).getName();
        }
        return result;
    }

    public Collection<String> getValues(String name)
    {
        return getValues();
    }

    public String getString()
    {
        StringBuffer result = new StringBuffer();
        for (String filename : getValues())
        {
            result.append(filename).append("\n");
        }
        return result.toString();
    }

    public Collection<String> getValues()
    {
        List<String> result = new ArrayList<String>();
        for (FTPFile ftpFile : getFiles().values())
        {
            result.add(ftpFile.getName());
        }
        Collections.sort(result, new FileNameComparator());
        return result;
    }

    public Map<String, FTPFile> getFiles()
    {
        if (files == null)
        {
            files = new HashMap<String, FTPFile>();
        }
        return files;
    }

    private class FileNameComparator implements Comparator<String>
    {
        public int compare(String o1, String o2)
        {
            return o1.compareTo(o2);
        }
    }

    @Override
    public String toString()
    {
        return StringUtil.toString(getValues());
    }
}
