package org.mash.harness.ftp.operations;

import org.apache.commons.net.ftp.FTPClient;
import org.mash.config.HarnessDefinition;
import org.mash.file.FileLoader;
import org.mash.harness.RunResponse;
import org.mash.harness.ftp.FTPOperation;

import java.io.File;
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
    private File path;

    public PutOperation(String fileName, HarnessDefinition definition)
    {
        this.fileName = fileName;
        if (definition != null && definition.getScriptDefinition() != null)
        {
            this.path = definition.getScriptDefinition().getPath();
        }
    }

    public RunResponse operate(FTPClient client, String ftpParams) throws Exception
    {
        InputStream fileStream = null;
        RunResponse result;
        if (fileName != null)
        {
            try
            {
                fileStream = loader.findStream(fileName, path);
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
