package org.mash.harness.ftp.operations;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;
import org.mash.harness.ftp.FTPOperation;
import org.mash.harness.ftp.ListRunResponse;

import java.util.Collection;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 3:39:40 PM
 *
 */
public class DeleteOperation implements FTPOperation
{
    private static final Logger log = Logger.getLogger(DeleteOperation.class.getName());

    public RunResponse operate(FTPClient client, String ftpParams) throws Exception
    {
        ListRunResponse response = (ListRunResponse) new ListOperation().operate(client, ftpParams);
        Collection<FTPFile> files = response.getFiles().values();

        //need to move to directory we just listed
        if (isDirectory(files, ftpParams))
        {
            client.changeWorkingDirectory(ftpParams);
        }

        for (FTPFile file : files)
        {
            log.info("Deleting " + file.getName());
            client.deleteFile(file.getName());
        }
        return new ListOperation().operate(client, ftpParams);
    }

    private boolean isDirectory(Collection<FTPFile> files, String ftpParams)
    {
        boolean result = false;
        if (files.size() > 1)
        {
            result = true;
        }
        else
        {
            if (files.size() > 0)
            {
                String fileName = files.iterator().next().getName();
                if (fileName != null && !fileName.equals(ftpParams))
                {
                    result = true;
                }
            }
        }
        return result;
    }
}
