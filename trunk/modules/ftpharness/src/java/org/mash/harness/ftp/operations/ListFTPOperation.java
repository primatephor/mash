package org.mash.harness.ftp.operations;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.mash.harness.RunResponse;
import org.mash.harness.ftp.FTPOperation;
import org.mash.harness.ftp.ListRunResponse;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 11:57:50 AM
 *
 */
public class ListFTPOperation implements FTPOperation
{
    public RunResponse operate(FTPClient client, String ftpParams) throws Exception
    {
        FTPFile[] files;
        if (ftpParams != null)
        {
            files = client.listFiles(ftpParams);
        }
        else
        {
            files = client.listFiles();
        }
        return new ListRunResponse(files);
    }
}
