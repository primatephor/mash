package org.mash.loader.accessor;

import org.mash.config.BaseParameter;
import org.mash.loader.ContentAccessor;

/**
 * Access system properties defined by Parameters and append any values that may be present already.
 *
 * For example, a url might look like 'http://my.test.system/url' as a property value, but when combined with a value
 * accessor, you can add more specific urls by having the value set to something like '/my/more/specific/url'
 * The end result: 'http://my.test.system/url/my/more/specific/url'
 *
 * @author teastlack
 * @since Jul 10, 2009 11:06:55 AM
 *
 */
public class PropertyAccessor implements ContentAccessor
{
    public String accessContent(BaseParameter parameter, String currentContent)
    {
        StringBuffer result = new StringBuffer();
        if (parameter.getProperty() != null &&
            parameter.getProperty().length() > 0)
        {
            String property = System.getProperty(parameter.getProperty());
            if (property != null)
            {
                result.append(property);
            }
            if (currentContent != null)
            {
                result.append(currentContent);
            }
        }
        else if (currentContent != null)
        {
            result.append(currentContent);
        }
        return result.toString();
    }
}
