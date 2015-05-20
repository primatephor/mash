package org.mash.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The entire text file is read into memory, so play nice.
 * <p/>
 *  Date: Jul 1, 2009 Time: 10:56:54 AM
 */
public class TextFileReader extends FileContentReader
{
    public static String separator = System.getProperty("line.separator");

    /**
     * Retrieve the contents of the specified stream
     *
     * @param stream to retrieve
     * @return string contents of the file
     * @throws FileReaderException when something goes wrong
     */
    public String getContents(InputStream stream) throws FileReaderException
    {
        StringBuilder contents = new StringBuilder();
        BufferedReader input = null;
        try
        {
            if (stream != null)
            {
                input = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = input.readLine()) != null)
                {
                    contents.append(line).append(separator);
                }
            }
        }
        catch (IOException e)
        {
            throw new FileReaderException("Problem reading from file stream", e);
        }
        finally
        {
            if (input != null)
            {
                try
                {
                    input.close();
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
