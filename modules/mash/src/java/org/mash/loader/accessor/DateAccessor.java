package org.mash.loader.accessor;

import org.mash.config.BaseParameter;
import org.mash.loader.ContentAccessor;

/**
 * The Date accessor just retreives the date as a string according to the format specified by the Date configuration.
 *
 * @author
 * @since Jul 10, 2009 4:29:12 PM
 *
 */
public class DateAccessor implements ContentAccessor
{
    public String accessContent(BaseParameter parameter, String currentContent) throws Exception
    {
        String results = "";
        if (currentContent != null)
        {
            results = currentContent;
        }
        if (parameter.getDate() != null)
        {
            results = parameter.getDate().asFormat();
        }
        return results;
    }
}
