package org.mash.file;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author
 * @since Sep 22, 2009 3:10:38 PM
 *
 */
public class ByteFileReader extends FileContentReader
{
    public String getContents(InputStream stream) throws FileReaderException
    {
        StringBuilder contents = new StringBuilder();
        BufferedInputStream bufferedInput = null;
        try
        {
            byte[] buffer = new byte[1024];
            bufferedInput = new BufferedInputStream(stream);

            int bytesRead;
            while ((bytesRead = bufferedInput.read(buffer)) != -1)
            {
                String chunk = new String(buffer, 0, bytesRead);
                contents.append(chunk);
            }
        }
        catch (IOException e)
        {
            throw new FileReaderException("Problem reading from file stream", e);
        }
        finally
        {
            if (bufferedInput != null)
            {
                try
                {
                    bufferedInput.close();
                }
                catch (IOException e)
                {
                    //yes, this could mask exceptions above
                    throw new FileReaderException("Problem closing file", e);
                }
            }
        }
        return contents.toString();
    }
}
