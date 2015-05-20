package org.mash.loader;

import org.mash.config.BaseParameter;

/**
 * Implementations of this interface will know how to access appropriate content (response, date, etc) from the
 * BaseParameter.  Extensions of the BaseParameter can then have their own ContentAccessor to retrieve their specific
 * content should that change.
 *
 * @author
 * @since Jul 10, 2009 10:25:12 AM
 *
 */
public interface ContentAccessor
{
    /**
     * Access the content, if possible, of the parameter in a special way defined by the implementation.  If the param
     * is to retrieve a file, then this would load that file and return it's contents.
     *
     * @param parameter to access
     * @param currentContent content already calculated by other accessors
     * @return null if unable to access, or content of the parameter
     */
    String accessContent(BaseParameter parameter, String currentContent) throws Exception;
}
