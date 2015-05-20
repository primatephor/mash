package org.mash.loader.accessor;

import org.apache.log4j.Logger;
import org.mash.config.BaseParameter;
import org.mash.config.Replace;
import org.mash.config.Replaceable;
import org.mash.loader.AccessorChain;
import org.mash.loader.ContentAccessor;

/**
 * Replace the content with any values found by the replacement.
 * <p/>
 * This works by pulling each <Replace> element out of the parameter, using an AccessChain to retrieve the value (since
 * it could be a Date, Value, file, or another response) and performing a replace on the currentContent.
 * <p/>
 * Since <Replace> elements are embedded, the content should have been found by previously run accessors, and this
 * accessor should come at the END of an accessor chain.  Replace accessors should not contain other replace accessors,
 * could get ugly.
 *
 * @author
 * @since Jul 10, 2009 1:32:30 PM
 */
public class ReplaceAccessor implements ContentAccessor
{
    private static final Logger log = Logger.getLogger(ReplaceAccessor.class.getName());
    private AccessorChain accessorChain;

    public ReplaceAccessor(AccessorChain accessorChain)
    {
        this.accessorChain = accessorChain;
    }

    public String accessContent(BaseParameter parameter,
                                String currentContent) throws Exception
    {
        String result = "";
        if (currentContent != null)
        {
            result = currentContent;
            if (parameter instanceof Replaceable)
            {
                log.trace("Running replace on parameter " + parameter.getName());
                Replaceable replaceable = (Replaceable) parameter;
                if (replaceable.getReplace() != null)
                {
                    for (Replace replace : replaceable.getReplace())
                    {
                        String value = accessorChain.access(replace);
                        log.debug("Replacing '" + replace.getSearch() + "' with '" + value + "'");
                        result = result.replace(replace.getSearch(), value);
                    }
                }
            }
        }
        return result;
    }
}
