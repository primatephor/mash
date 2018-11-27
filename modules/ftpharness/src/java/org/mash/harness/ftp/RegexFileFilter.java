package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

public class RegexFileFilter implements FTPFileFilter
{
    private String pattern;

    public RegexFileFilter(String pattern )
    {
        this.pattern = pattern;
    }

    @Override
    public boolean accept(FTPFile ftpFile)
    {
        if( pattern != null && ! pattern.isEmpty())
        {
            return ftpFile.getName().matches( pattern );
        }
        else
        {
            return true;
        }
    }
}
