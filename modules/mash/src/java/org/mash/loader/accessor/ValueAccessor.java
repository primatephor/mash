package org.mash.loader.accessor;

import org.mash.config.BaseParameter;
import org.mash.loader.ContentAccessor;

/**
 * The basic and simple accessor, just retrieve the value of the parameter.  If there is previous content, current
 * value of this parameter is appended to it.
 *
 * @author
 * @since Jul 10, 2009 10:34:56 AM
 *
 */
public class ValueAccessor implements ContentAccessor
{
    public String accessContent(BaseParameter parameter, String currentContent)
    {
        StringBuffer result = new StringBuffer();
        if (currentContent != null)
        {
            result.append(currentContent);
        }
        if (parameter.getValue() != null)
        {
            result.append(parameter.getValue());
        }
        return result.toString();
    }
}
