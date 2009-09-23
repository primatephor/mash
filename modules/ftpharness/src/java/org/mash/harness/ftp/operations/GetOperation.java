package org.mash.harness.ftp.operations;

import org.apache.commons.net.ftp.FTPClient;
import org.mash.file.ByteFileReader;
import org.mash.harness.RunResponse;
import org.mash.harness.ftp.FTPOperation;
import org.mash.harness.ftp.FileContentsResponse;
import org.mash.harness.ftp.FileRefResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 2:32:46 PM
 *
 */
public class GetOperation implements FTPOperation
{
    private String fileName;

    public GetOperation()
    {
    }

    public GetOperation(String fileName)
    {
        this.fileName = fileName;
    }

    public RunResponse operate(FTPClient client, String ftpParams) throws Exception
    {
        InputStream fileStream = null;
        RunResponse result;
        try
        {
            fileStream = client.retrieveFileStream(ftpParams);
            if (fileName == null)
            {
                String fileContents = new ByteFileReader().getContents(fileStream);
                result = new FileContentsResponse(fileContents);
            }
            else
            {
                File output = new File(fileName);
                OutputStream out = new FileOutputStream(output);
                byte buf[] = new byte[1024];
                int len;
                while ((len = fileStream.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }
                out.close();
                result = new FileRefResponse(output);
            }
        }
        finally
        {
            if (fileStream != null)
            {
                try
                {
                    fileStream.close();
                }
                catch (IOException e)
                {
                    //yes, this could mask exceptions above
                    throw new OperationException("Problem closing file", e);
                }
            }
        }
        if (!client.completePendingCommand())
        {
            throw new OperationException("File transfer incomplete!");
        }
        return result;
    }
}
