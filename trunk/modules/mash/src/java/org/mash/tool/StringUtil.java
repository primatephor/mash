package org.mash.tool;

/**
 * Some simple utilities
 *
 * @author teastlack
 * @since Sep 9, 2010 10:26:21 AM
 */
public class StringUtil
{
    public static String cleanNull(String value)
    {
        String result = "";
        if (value != null)
        {
            result = value;
        }
        return result;
    }
}
