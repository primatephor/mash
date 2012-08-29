package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 *
 * @author teastlack
 * @since Sep 30, 2009 8:04:08 PM
 *
 */
public class BogusRawFTPClient extends FTPClient
{
    private String command;
    private String args;
    private String[] results;

    public BogusRawFTPClient(String[] results)
    {
        this.results = results;
    }

    public int sendCommand(String s, String s1) throws IOException
    {
        command = s;
        args = s1;
        return 1;
    }

    public String[] getReplyStrings()
    {
        return results;
    }

    public String getCommand()
    {
        return command;
    }

    public String getArgs()
    {
        return args;
    }
}
