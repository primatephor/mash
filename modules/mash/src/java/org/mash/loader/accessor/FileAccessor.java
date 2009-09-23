package org.mash.loader.accessor;

import org.mash.config.BaseParameter;
import org.mash.file.FileLoader;
import org.mash.file.TextFileReader;
import org.mash.loader.ContentAccessor;

import java.io.File;

/**
 * Reads the contents of a file and returns the results.  This ignores any current content (replaces it).  This assumes
 * that content is STRING only, so this couldn't be used for binary files nicely.
 *
 * @author teastlack
 * @since Jul 10, 2009 12:23:08 PM
 *
 */
public class FileAccessor implements ContentAccessor
{
    private File basePath;
    private TextFileReader reader = new TextFileReader();

    public FileAccessor(File basePath)
    {
        this.basePath = basePath;
    }

    public String accessContent(BaseParameter parameter, String currentContent) throws Exception
    {
        String result = "";
        if (currentContent != null)
        {
            result = currentContent;
        }
        if (parameter.getFile() != null)
        {
            File theFile = new FileLoader().findFile(parameter.getFile(), basePath);
            result = reader.getContents(theFile);
        }
        return result;
    }
}
