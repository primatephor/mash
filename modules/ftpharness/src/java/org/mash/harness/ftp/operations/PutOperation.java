package org.mash.harness.ftp.operations;

import org.apache.commons.net.ftp.FTPClient;
import org.mash.file.FileLoader;
import org.mash.harness.RunResponse;
import org.mash.harness.ftp.FTPOperation;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 2:26:48 PM
 *
 */
public class PutOperation implements FTPOperation
{
    private FileLoader loader = new FileLoader();
    private String fileName;

    public PutOperation(String fileName)
    {
        this.fileName = fileName;
    }

    public RunResponse operate(FTPClient client, String ftpParams) throws Exception
    {
        InputStream fileStream = null;
        RunResponse result;
        if (fileName != null)
        {
            try
            {
                fileStream = loader.findStream(fileName);
                client.storeFile(ftpParams, fileStream);
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
                        throw new OperationException("Problem reading file", e);
                    }
                }
            }
            //list the file as a response
            result = new ListOperation().operate(client, ftpParams);
        }
        else
        {
            throw new OperationException("No file specified to read");
        }
        return result;
    }
}
